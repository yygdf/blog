package com.iksling.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iksling.blog.dto.*;
import com.iksling.blog.entity.Article;
import com.iksling.blog.entity.ArticleTag;
import com.iksling.blog.entity.Category;
import com.iksling.blog.entity.Tag;
import com.iksling.blog.mapper.ArticleMapper;
import com.iksling.blog.mapper.ArticleTagMapper;
import com.iksling.blog.mapper.CategoryMapper;
import com.iksling.blog.mapper.TagMapper;
import com.iksling.blog.pojo.LoginUser;
import com.iksling.blog.service.ArticleService;
import com.iksling.blog.service.ArticleTagService;
import com.iksling.blog.util.BeanCopyUtil;
import com.iksling.blog.util.UserUtil;
import com.iksling.blog.vo.ArticleBackVO;
import com.iksling.blog.vo.ArticlesGarbageVO;
import com.iksling.blog.vo.ConditionVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.iksling.blog.constant.RedisConst.ARTICLE_LIKE_COUNT;
import static com.iksling.blog.constant.RedisConst.ARTICLE_VIEW_COUNT;

/**
 *
 */
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article>
    implements ArticleService{
    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private ArticleTagMapper articleTagMapper;
    @Autowired
    private TagMapper tagMapper;
    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private ArticleService articleService;
    @Autowired
    private ArticleTagService articleTagService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public ArticleBackDTO getArticleBackDTOById(Integer articleId) {
        Integer userId = UserUtil.getLoginUser().getUserId();
        Article article = articleMapper.selectOne(new LambdaQueryWrapper<Article>()
                .select(Article::getId, Article::getUserId, Article::getCategoryId, Article::getArticleTitle, Article::getArticleCover, Article::getArticleContent,
                        Article::getTopFlag, Article::getDraftFlag, Article::getPublicFlag, Article::getHiddenFlag, Article::getCommentableFlag)
                .eq(Article::getId, articleId)
                .eq(Article::getUserId, userId)
                .eq(Article::getGarbageFlag, 0)
                .eq(Article::getDeletedFlag, 0));
        List<Integer> tagIdList = articleTagMapper.selectList(new LambdaQueryWrapper<ArticleTag>()
                .select(ArticleTag::getTagId)
                .eq(ArticleTag::getArticleId, articleId))
                .stream().map(ArticleTag::getTagId).collect(Collectors.toList());
        ArticleBackDTO articleBackDTO = BeanCopyUtil.copyObject(article, ArticleBackDTO.class);
        articleBackDTO.setTagIdList(tagIdList);
        return articleBackDTO;
    }

    @Override
    public ArticleOptionDTO getArticleOptionDTO() {
        Integer userId = UserUtil.getLoginUser().getUserId();
        List<Tag> tagList = tagMapper.selectList(new LambdaQueryWrapper<Tag>()
                .select(Tag::getId, Tag::getUserId, Tag::getTagName)
                .eq(Tag::getUserId, userId));
        List<TagDTO> tagDTOList = BeanCopyUtil.copyList(tagList, TagDTO.class);
        List<Category> categoryList = categoryMapper.selectList(new LambdaQueryWrapper<Category>()
                .select(Category::getId, Category::getUserId, Category::getCategoryName)
                .eq(Category::getUserId, userId));
        List<CategoryDTO> categoryDTOList = BeanCopyUtil.copyList(categoryList, CategoryDTO.class);
        return ArticleOptionDTO.builder()
                .userId(userId)
                .tagDTOList(tagDTOList)
                .categoryDTOList(categoryDTOList)
                .build();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer saveOrUpdateArticleBackVO(ArticleBackVO articleBackVO) {
        LoginUser loginUser =  UserUtil.getLoginUser();
        Article article = BeanCopyUtil.copyObject(articleBackVO, Article.class);
        if (Objects.isNull(article.getId())) {
            article.setUserId(loginUser.getUserId());
            article.setDeletedFlag(false);
            article.setGarbageFlag(false);
            article.setIpSource(loginUser.getIpSource());
            article.setIpAddress(loginUser.getIpAddress());
            article.setCreateUser(loginUser.getUserId());
            article.setCreateTime(new Date());
        } else if (!article.getDraftFlag()) {
            article.setUpdateUser(loginUser.getUserId());
            article.setUpdateTime(new Date());
            articleTagMapper.delete(new LambdaQueryWrapper<ArticleTag>()
                    .eq(ArticleTag::getArticleId, article.getId()));
        }
        articleService.saveOrUpdate(article);
        if (!articleBackVO.getTagIdList().isEmpty()) {
            List<ArticleTag> articleTagList = articleBackVO.getTagIdList().stream().map(tagId -> ArticleTag.builder()
                    .articleId(article.getId())
                    .tagId(tagId)
                    .build()).collect(Collectors.toList());
            articleTagService.saveBatch(articleTagList);
        }
        return article.getId();
    }

    @Override
    public PageDTO<ArticlesBackDTO> getPageArticlesBackDTO(ConditionVO condition) {
        condition.setCurrent((condition.getCurrent() - 1) * condition.getSize());
        Integer count = articleMapper.selectCountByCondition(condition);
        if (count == 0)
            return new PageDTO<>();
        List<ArticlesBackDTO> articlesBackDTOList = articleMapper.listArticlesBackDTO(condition);
        Map<String, Integer> viewCountMap = redisTemplate.boundHashOps(ARTICLE_VIEW_COUNT).entries();
        Map<String, Integer> likeCountMap = redisTemplate.boundHashOps(ARTICLE_LIKE_COUNT).entries();
        articlesBackDTOList.forEach(item -> {
            item.setViewCount(Objects.requireNonNull(viewCountMap).get(item.getId().toString()));
            item.setLikeCount(Objects.requireNonNull(likeCountMap).get(item.getId().toString()));
        });
        return new PageDTO<>(count, articlesBackDTOList);
    }

    @Override
    @Transactional
    public void updateArticlesDeleteVO(ArticlesGarbageVO articlesGarbageVO) {
        List<Article> articleList = articlesGarbageVO.getIdList().stream().map(id -> Article.builder()
                .id(id)
                .topFlag(false)
                .garbageFlag(articlesGarbageVO.getGarbageFlag())
                .build())
                .collect(Collectors.toList());
        articleService.updateBatchById(articleList);
    }

    @Override
    @Transactional
    public void deleteArticleIdList(List<Integer> articleIdList) {
        List<Article> articleList = articleIdList.stream().map(id -> Article.builder()
                .id(id)
                .deletedFlag(true)
                .build())
                .collect(Collectors.toList());
        articleService.updateBatchById(articleList);
    }

    @Override
    @Transactional
    public void updateArticleTopById(Integer articleId, Boolean topFlag) {
        articleMapper.updateById(Article.builder()
            .id(articleId)
            .topFlag(topFlag)
            .build());
    }

    @Override
    @Transactional
    public void updateArticlePublicById(Integer articleId, Boolean publicFlag) {
        articleMapper.updateById(Article.builder()
                .id(articleId)
                .publicFlag(publicFlag)
                .build());
    }

    @Override
    @Transactional
    public void updateArticleHiddenById(Integer articleId, Boolean hiddenFlag) {
        articleMapper.updateById(Article.builder()
                .id(articleId)
                .hiddenFlag(hiddenFlag)
                .build());
    }

    @Override
    @Transactional
    public void updateArticleCommentableById(Integer articleId, Boolean commentableFlag) {
        articleMapper.updateById(Article.builder()
                .id(articleId)
                .commentableFlag(commentableFlag)
                .build());
    }
}




