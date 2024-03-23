package com.iksling.blog.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.iksling.blog.entity.*;
import com.iksling.blog.exception.OperationStatusException;
import com.iksling.blog.mapper.*;
import com.iksling.blog.pojo.LoginUser;
import com.iksling.blog.service.BlogService;
import com.iksling.blog.util.UserUtil;
import com.iksling.blog.vo.TokenVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.stream.Collectors;

import static com.iksling.blog.constant.CommonConst.ROOT_USER_ID;
import static com.iksling.blog.constant.RedisConst.*;

/**
 *
 */
@Service
public class BlogServiceImpl implements BlogService {
    @Autowired
    private UserAuthMapper userAuthMapper;
    @Autowired
    private UserConfigMapper userConfigMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private TagMapper tagMapper;

    @Autowired
    private RedisTemplate redisTemplate;
    @Resource
    private HttpServletRequest request;

    @Autowired
    private SystemConfigServiceImpl systemConfigServiceImpl;
    @Autowired
    private UserConfigServiceImpl userConfigServiceImpl;

    @Override
    @Transactional
    public void updateBackAbout(String aboutContent) {
        Object o = JSON.parseObject(aboutContent, Map.class).get("aboutContent");
        if (o != null)
            redisTemplate.boundHashOps(BLOG_ABOUT_ME).put(UserUtil.getLoginUser().getUserId().toString(), o);
    }

    @Override
    @Transactional
    public Object saveTokenVO(TokenVO tokenVO) {
        LoginUser loginUser = UserUtil.getLoginUser();
        Integer loginUserId = loginUser.getUserId();
        Integer id = tokenVO.getId();
        if (tokenVO.getType() == null) {
            BoundHashOperations boundHashOperations = redisTemplate.boundHashOps(ARTICLE_TOKEN + "_" + id);
            Map<String, Object> map = boundHashOperations.entries();
            if (map == null)
                throw new OperationStatusException("密令不存在!");
            if (!tokenVO.getAccessToken().equals(map.get("accessToken")))
                throw new OperationStatusException("密令不存在!");
            Integer count = (Integer) map.get("effectiveCount");
            if (count == 0)
                throw new OperationStatusException("密令已失效!");
            HashSet<Integer> articleTokenSet = (HashSet<Integer>) redisTemplate.boundHashOps(ARTICLE_TOKEN).get(loginUserId.toString());
            if (articleTokenSet == null)
                articleTokenSet = new HashSet<>();
            if (articleTokenSet.contains(id))
                return null;
            articleTokenSet.add(id);
            redisTemplate.boundHashOps(ARTICLE_TOKEN).put(loginUserId.toString(), articleTokenSet);
            if (count != -1)
                redisTemplate.boundHashOps(ARTICLE_TOKEN + "_" + id).increment("effectiveCount", -1);
            return articleMapper.selectObjs(new LambdaQueryWrapper<Article>().select(Article::getArticleContent).eq(Article::getId, id)).get(0);
        }
        return null;
    }

    @Override
    public String getAbout() {
        String bloggerId = request.getHeader("Blogger-Id");
        if (bloggerId != null) {
            Object aboutContent = redisTemplate.boundHashOps(BLOG_ABOUT_ME).get(bloggerId);
            return aboutContent == null ? "" : aboutContent.toString();
        }
        Object aboutContent = redisTemplate.boundHashOps(BLOG_ABOUT_ME).get(UserUtil.getLoginUser().getUserId().toString());
        return aboutContent == null ? "" : aboutContent.toString();
    }

    @Override
    public Integer getBlogId(Integer bloggerId) {
        if (bloggerId == null)
            return ROOT_USER_ID;
        Integer count = userAuthMapper.selectCount(new LambdaQueryWrapper<UserAuth>()
                .eq(UserAuth::getUserId, bloggerId)
                .eq(UserAuth::getAssimilateNowFlag, true));
        if (count == 0)
            return ROOT_USER_ID;
        return bloggerId;
    }

    @Override
    public HashMap<String, Object> getBlogInfo() {
        HashMap<String, Object> hashMap = new HashMap<>();
        HashMap<String, Object> blogConfigMap;
        HashMap<String, Object> bloggerInfoMap = new HashMap<>();
        Integer bloggerId = Integer.valueOf(request.getHeader("Blogger-Id"));
        if (bloggerId.equals(ROOT_USER_ID) || !"true".equals(systemConfigServiceImpl.getSystemConfigMap().get("enable_user_config"))) {
            blogConfigMap = new HashMap<>(userConfigServiceImpl.getUserConfigMap());
        } else {
            blogConfigMap = userConfigMapper.selectList(new LambdaQueryWrapper<UserConfig>()
                    .select(UserConfig::getConfigName, UserConfig::getConfigValue)
                    .eq(UserConfig::getUserId, bloggerId)
                    .eq(UserConfig::getDeletedFlag, false))
                    .stream()
                    .collect(Collectors.toMap(UserConfig::getConfigName, UserConfig::getConfigValue, (key1, key2) -> key2, HashMap::new));
        }
        User blogger = userMapper.selectOne(new LambdaQueryWrapper<User>()
                .select(User::getAvatar, User::getNickname, User::getIntro)
                .eq(User::getId, bloggerId));
        Integer articleCount = articleMapper.selectCount(new LambdaQueryWrapper<Article>()
                .eq(Article::getDraftFlag, false)
                .eq(Article::getRecycleFlag, false)
                .eq(Article::getDeletedFlag, false));
        Integer categoryCount = categoryMapper.selectCount(new LambdaQueryWrapper<Category>()
                .eq(Category::getDeletedFlag, false));
        Integer tagCount = tagMapper.selectCount(new LambdaQueryWrapper<Tag>()
                .eq(Tag::getDeletedFlag, false));
        Object viewCount = redisTemplate.boundHashOps(BLOG_VIEW_COUNT).get(bloggerId.toString());
        bloggerInfoMap.put("avatar", blogger.getAvatar());
        bloggerInfoMap.put("nickname", blogger.getNickname());
        bloggerInfoMap.put("intro", blogger.getIntro());
        bloggerInfoMap.put("articleCount", articleCount);
        bloggerInfoMap.put("categoryCount", categoryCount);
        bloggerInfoMap.put("tagCount", tagCount);
        bloggerInfoMap.put("viewCount", viewCount);
        hashMap.put("bloggerId", bloggerId);
        hashMap.put("blogConfig", blogConfigMap);
        hashMap.put("bloggerInfo", bloggerInfoMap);
        return hashMap;
    }
}




