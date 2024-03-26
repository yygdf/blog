package com.iksling.blog.listener;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.iksling.blog.dto.ArticlesSearchDTO;
import com.iksling.blog.entity.Article;
import com.iksling.blog.entity.UserAuth;
import com.iksling.blog.mapper.ElasticsearchMapper;
import com.iksling.blog.mapper.UserAuthMapper;
import com.iksling.blog.util.BeanCopyUtil;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

import static com.iksling.blog.constant.MQConst.ARTICLE_QUEUE;

@Component
@RabbitListener(queues = ARTICLE_QUEUE)
public class ArticleQueueListener {
    @Autowired
    private UserAuthMapper userAuthMapper;

    @Autowired
    private ElasticsearchMapper elasticsearchMapper;

    @RabbitHandler
    public void process(byte[] data) {
        Map<String, Object> map = JSON.parseObject(new String(data), Map.class);
        Article article = JSONObject.toJavaObject((JSONObject) map.get("data"), Article.class);
        String type = map.get("type").toString();
        switch (type) {
            case "insert":
                if (!article.getDraftFlag() && article.getPublicFlag() && !article.getHiddenFlag()) {
                    ArticlesSearchDTO articlesSearchDTO = BeanCopyUtil.copyObject(article, ArticlesSearchDTO.class);
                    articlesSearchDTO.setUsername(userAuthMapper.selectObjs(new LambdaQueryWrapper<UserAuth>()
                            .select(UserAuth::getUsername)
                            .eq(UserAuth::getUserId, articlesSearchDTO.getUserId())).get(0).toString());
                    elasticsearchMapper.save(articlesSearchDTO);
                }
                break;
            case "update":
                if (!article.getDraftFlag() && article.getPublicFlag() && !article.getHiddenFlag()) {
                    ArticlesSearchDTO articlesSearchDTO = BeanCopyUtil.copyObject(article, ArticlesSearchDTO.class);
                    articlesSearchDTO.setUsername(userAuthMapper.selectObjs(new LambdaQueryWrapper<UserAuth>()
                            .select(UserAuth::getUsername)
                            .eq(UserAuth::getUserId, articlesSearchDTO.getUserId())).get(0).toString());
                    elasticsearchMapper.save(articlesSearchDTO);
                } else {
                    Optional<ArticlesSearchDTO> optional = elasticsearchMapper.findById(article.getId());
                    if (optional.isPresent()) {
                        ArticlesSearchDTO articlesSearchDTO = optional.get();
                        if (!articlesSearchDTO.getDraftFlag() && articlesSearchDTO.getPublicFlag() && !articlesSearchDTO.getHiddenFlag()) {
                            articlesSearchDTO = BeanCopyUtil.copyObject(article, ArticlesSearchDTO.class);
                            articlesSearchDTO.setUsername(userAuthMapper.selectObjs(new LambdaQueryWrapper<UserAuth>()
                                    .select(UserAuth::getUsername)
                                    .eq(UserAuth::getUserId, articlesSearchDTO.getUserId())).get(0).toString());
                            elasticsearchMapper.save(articlesSearchDTO);
                        }
                    }
                }
                break;
            case "delete":
                elasticsearchMapper.deleteById(article.getId());
            default:
                break;
        }
    }
}
