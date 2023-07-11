package com.iksling.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iksling.blog.dto.*;
import com.iksling.blog.entity.Article;
import com.iksling.blog.entity.ArticleTag;
import com.iksling.blog.entity.Category;
import com.iksling.blog.entity.Tag;
import com.iksling.blog.enums.FilePathEnum;
import com.iksling.blog.exception.IllegalRequestException;
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
import com.iksling.blog.vo.ArticleStatusVO;
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

import static com.iksling.blog.constant.CommonConst.STATIC_RESOURCE_URL;
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
        } else {
            Integer count = articleMapper.selectCount(new LambdaQueryWrapper<Article>()
                .eq(Article::getId, article.getId())
                .eq(Article::getUserId, loginUser.getUserId()));
            if (count != 1)
                throw new IllegalRequestException("你不要瞎搞, 小心我顺着网线爬过去找你!");
            if (!article.getDraftFlag()) {
                article.setUpdateUser(loginUser.getUserId());
                article.setUpdateTime(new Date());
            }
            articleTagMapper.delete(new LambdaQueryWrapper<ArticleTag>()
                    .eq(ArticleTag::getArticleId, article.getId()));
        }
        if (StringUtils.isBlank(article.getArticleCover()))
            article.setArticleCover(STATIC_RESOURCE_URL + FilePathEnum.ARTICLE.getPath() + loginUser.getUserId() + "/default/defaultCover.png");
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
        Integer userId = UserUtil.getLoginUser().getUserId();
        Integer count = articleMapper.selectCountByCondition(condition, userId);
        if (count == 0)
            return new PageDTO<>();
        List<ArticlesBackDTO> articlesBackDTOList = articleMapper.listArticlesBackDTO(condition, userId);
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
    public void updateArticlesGarbageVO(ArticlesGarbageVO articlesGarbageVO) {
        articleMapper.updateArticlesGarbageVO(articlesGarbageVO, UserUtil.getLoginUser().getUserId());
    }

    @Override
    @Transactional
    public void deleteArticleIdList(List<Integer> articleIdList) {
        ArticlesGarbageVO articlesGarbageVO = new ArticlesGarbageVO();
        articlesGarbageVO.setIdList(articleIdList);
        articleMapper.updateArticlesGarbageVO(articlesGarbageVO, UserUtil.getLoginUser().getUserId());
    }

    @Override
    @Transactional
    public void updateArticleStatusVO(ArticleStatusVO articleStatusVO) {
        articleMapper.update(null, new LambdaUpdateWrapper<Article>()
                .set(Article::getTopFlag, articleStatusVO.getTopFlag())
                .set(Article::getPublicFlag, articleStatusVO.getPublicFlag())
                .set(Article::getHiddenFlag, articleStatusVO.getHiddenFlag())
                .set(Article::getCommentableFlag, articleStatusVO.getCommentableFlag())
                .eq(Article::getId, articleStatusVO.getId())
                .eq(Article::getUserId, UserUtil.getLoginUser().getUserId()));
    }
}




