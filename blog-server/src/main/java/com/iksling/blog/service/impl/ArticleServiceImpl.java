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
import com.iksling.blog.pojo.PagePojo;
import com.iksling.blog.service.ArticleService;
import com.iksling.blog.service.ArticleTagService;
import com.iksling.blog.util.BeanCopyUtil;
import com.iksling.blog.util.UserUtil;
import com.iksling.blog.vo.ArticleBackVO;
import com.iksling.blog.vo.CommonStatusVO;
import com.iksling.blog.vo.UpdateBatchVO;
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
    private ArticleTagService articleTagService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public ArticleBackDTO getArticleBackDTOById(Integer articleId) {
        LoginUser loginUser = UserUtil.getLoginUser();
        Article article = articleMapper.selectOne(new LambdaQueryWrapper<Article>()
                .select(Article::getId, Article::getUserId, Article::getCategoryId, Article::getArticleTitle, Article::getArticleCover, Article::getArticleContent,
                        Article::getTopFlag, Article::getDraftFlag, Article::getPublicFlag, Article::getHiddenFlag, Article::getCommentableFlag)
                .eq(Article::getId, articleId)
                .eq(loginUser.getRoleWeight() > 300, Article::getUserId, loginUser.getUserId())
                .eq(Article::getRecycleFlag, 0)
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
    public ArticleOptionDTO getArticleOptionDTO(Integer userId) {
        LoginUser loginUser = UserUtil.getLoginUser();
        if (Objects.nonNull(userId) && loginUser.getRoleWeight() > 300 && !loginUser.getUserId().equals(userId))
            throw new IllegalRequestException();
            List<Tag> tagList = tagMapper.selectList(new LambdaQueryWrapper<Tag>()
                .select(Tag::getId, Tag::getUserId, Tag::getTagName)
                .eq(Objects.isNull(userId), Tag::getUserId, loginUser.getUserId())
                .eq(Objects.nonNull(userId), Tag::getUserId, userId));
        List<TagDTO> tagDTOList = BeanCopyUtil.copyList(tagList, TagDTO.class);
        List<Category> categoryList = categoryMapper.selectList(new LambdaQueryWrapper<Category>()
                .select(Category::getId, Category::getUserId, Category::getCategoryName)
                .eq(Objects.isNull(userId), Category::getUserId, loginUser.getUserId())
                .eq(Objects.nonNull(userId), Category::getUserId, userId));
        List<CategoryDTO> categoryDTOList = BeanCopyUtil.copyList(categoryList, CategoryDTO.class);
        return ArticleOptionDTO.builder()
                .userId(loginUser.getUserId())
                .tagDTOList(tagDTOList)
                .categoryDTOList(categoryDTOList)
                .build();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer saveOrUpdateArticleBackVO(ArticleBackVO articleBackVO) {
        LoginUser loginUser =  UserUtil.getLoginUser();
        articleBackVO.setArticleTitle(articleBackVO.getArticleTitle().trim());
        Article article = BeanCopyUtil.copyObject(articleBackVO, Article.class);
        if (Objects.isNull(article.getId())) {
            article.setUserId(loginUser.getUserId());
            article.setRecycleFlag(false);
            article.setDeletedFlag(false);
            article.setIpSource(loginUser.getIpSource());
            article.setIpAddress(loginUser.getIpAddress());
            article.setCreateUser(loginUser.getUserId());
            article.setCreateTime(new Date());
            if (!article.getDraftFlag()) {
                Integer count = categoryMapper.selectCount(new LambdaQueryWrapper<Category>()
                        .eq(Category::getId, article.getCategoryId())
                        .eq(Category::getUserId, loginUser.getUserId()));
                if (count != 1)
                    throw new IllegalRequestException();
                article.setPublishUser(loginUser.getUserId());
                article.setPublishTime(new Date());
                if (StringUtils.isBlank(article.getArticleCover()))
                    article.setArticleCover(STATIC_RESOURCE_URL + FilePathEnum.ARTICLE.getPath() + loginUser.getUserId() + "/default/defaultCover.jpg");
            }
        } else {
            Article article2 = articleMapper.selectOne(new LambdaQueryWrapper<Article>()
                    .select(Article::getId, Article::getUserId, Article::getPublishUser)
                    .eq(Article::getId, article.getId())
                    .eq(loginUser.getRoleWeight() > 300, Article::getUserId, loginUser.getUserId()));
            if (Objects.isNull(article2.getId()))
                throw new IllegalRequestException();
            if (!article.getDraftFlag()) {
                Integer count = categoryMapper.selectCount(new LambdaQueryWrapper<Category>()
                        .eq(Category::getId, article.getCategoryId())
                        .eq(Category::getUserId, article2.getUserId()));
                if (count != 1)
                    throw new IllegalRequestException();
                article.setUpdateUser(loginUser.getUserId());
                article.setUpdateTime(new Date());
                if (Objects.isNull(article2.getPublishUser())) {
                    article.setPublishUser(loginUser.getUserId());
                    article.setPublishTime(new Date());
                }
                if (StringUtils.isBlank(article.getArticleCover()))
                    article.setArticleCover(STATIC_RESOURCE_URL + FilePathEnum.ARTICLE.getPath() + article2.getUserId() + "/default/defaultCover.jpg");
            }
            articleTagMapper.delete(new LambdaQueryWrapper<ArticleTag>()
                    .eq(ArticleTag::getArticleId, article.getId()));
            article.setUserId(article2.getUserId());
        }
        this.saveOrUpdate(article);
        if (!articleBackVO.getTagIdList().isEmpty()) {
            List<Tag> tagList = tagMapper.selectList(new LambdaQueryWrapper<Tag>()
                    .select(Tag::getId)
                    .eq(Tag::getUserId, article.getUserId()));
            if (!tagList.stream().map(Tag::getId).collect(Collectors.toList()).containsAll(articleBackVO.getTagIdList()))
                throw new IllegalRequestException();
            List<ArticleTag> articleTagList = articleBackVO.getTagIdList().stream().map(tagId -> ArticleTag.builder()
                    .articleId(article.getId())
                    .tagId(tagId)
                    .build()).collect(Collectors.toList());
            articleTagService.saveBatch(articleTagList);
        }
        return article.getId();
    }

    @Override
    public PagePojo<ArticlesBackDTO> getPageArticlesBackDTO(ConditionVO condition) {
        LoginUser loginUser = UserUtil.getLoginUser();
        if (loginUser.getRoleWeight() > 100 && Objects.equals(condition.getDeletedFlag(), true))
            throw new IllegalRequestException();
        condition.setCurrent((condition.getCurrent() - 1) * condition.getSize());
        List<ArticlesBackDTO> articlesBackDTOList = articleMapper.listArticlesBackDTO(condition, loginUser.getUserId(), loginUser.getRoleWeight());
        if (articlesBackDTOList.size() == 0)
            return new PagePojo<>();
        Map<String, Integer> viewCountMap = redisTemplate.boundHashOps(ARTICLE_VIEW_COUNT).entries();
        Map<String, Integer> likeCountMap = redisTemplate.boundHashOps(ARTICLE_LIKE_COUNT).entries();
        articlesBackDTOList.forEach(item -> {
            item.setViewCount(Objects.requireNonNull(viewCountMap).get(item.getId().toString()));
            item.setLikeCount(Objects.requireNonNull(likeCountMap).get(item.getId().toString()));
        });
        return new PagePojo<>(articlesBackDTOList.size(), articlesBackDTOList);
    }

    @Override
    @Transactional
    public void updateArticlesStatus(UpdateBatchVO updateBatchVO) {
        LoginUser loginUser = UserUtil.getLoginUser();
        Integer count = articleMapper.updateArticlesStatus(updateBatchVO, loginUser.getUserId(), loginUser.getRoleWeight());
        if (count != updateBatchVO.getIdList().size())
            throw new IllegalRequestException();
    }

    @Override
    @Transactional
    public void deleteArticleIdList(List<Integer> articleIdList) {
        if (UserUtil.getLoginUser().getRoleWeight() > 100 || articleIdList.size() == 0)
            throw new IllegalRequestException();
        int count = articleMapper.deleteBatchIds(articleIdList);
        if (count != articleIdList.size())
            throw new IllegalRequestException();
    }

    @Override
    @Transactional
    public void updateArticleStatusVO(CommonStatusVO commonStatusVO) {
        LoginUser loginUser = UserUtil.getLoginUser();
        int count = articleMapper.update(null, new LambdaUpdateWrapper<Article>()
                .set(Article::getTopFlag, Objects.isNull(commonStatusVO.getTopFlag()) ? false : commonStatusVO.getTopFlag())
                .set(Article::getPublicFlag, commonStatusVO.getPublicFlag())
                .set(Article::getHiddenFlag, commonStatusVO.getHiddenFlag())
                .set(Article::getCommentableFlag, Objects.isNull(commonStatusVO.getCommentableFlag()) ? false : commonStatusVO.getCommentableFlag())
                .eq(Article::getId, commonStatusVO.getId())
                .eq(loginUser.getRoleWeight() > 300, Article::getUserId, loginUser.getUserId()));
        if (count != 1)
            throw new IllegalRequestException();
    }
}




