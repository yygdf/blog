package com.iksling.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iksling.blog.dto.ArticleBackDTO;
import com.iksling.blog.dto.ArticleOptionDTO;
import com.iksling.blog.dto.ArticlesBackDTO;
import com.iksling.blog.dto.LabelDTO;
import com.iksling.blog.entity.*;
import com.iksling.blog.exception.IllegalRequestException;
import com.iksling.blog.exception.OperationStatusException;
import com.iksling.blog.mapper.*;
import com.iksling.blog.pojo.LoginUser;
import com.iksling.blog.pojo.PagePojo;
import com.iksling.blog.service.ArticleService;
import com.iksling.blog.service.ArticleTagService;
import com.iksling.blog.service.MultiFileService;
import com.iksling.blog.util.BeanCopyUtil;
import com.iksling.blog.util.IpUtil;
import com.iksling.blog.util.UserUtil;
import com.iksling.blog.vo.ArticleBackVO;
import com.iksling.blog.vo.CommonStatusVO;
import com.iksling.blog.vo.ConditionVO;
import com.iksling.blog.vo.UpdateBatchVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

import static com.iksling.blog.constant.CommonConst.STATIC_RESOURCE_URL;
import static com.iksling.blog.constant.RedisConst.ARTICLE_LIKE_COUNT;
import static com.iksling.blog.constant.RedisConst.ARTICLE_VIEW_COUNT;
import static com.iksling.blog.enums.FilePathEnum.IMG_ARTICLE;

/**
 *
 */
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article>
    implements ArticleService{
    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private TagMapper tagMapper;
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private ArticleTagMapper articleTagMapper;

    @Autowired
    private ArticleTagService articleTagService;
    @Autowired
    private MultiFileService multiFileService;

    @Autowired
    private RedisTemplate redisTemplate;
    @Resource
    private HttpServletRequest request;

    @Override
    public ArticleBackDTO getArticleBackDTOById(Integer articleId) {
        LoginUser loginUser = UserUtil.getLoginUser();
        Article article = articleMapper.selectOne(new LambdaQueryWrapper<Article>()
                .select(Article::getId, Article::getUserId, Article::getCategoryId, Article::getArticleTitle, Article::getArticleCover, Article::getArticleContent,
                        Article::getTopFlag, Article::getDraftFlag, Article::getPublicFlag, Article::getHiddenFlag, Article::getCommentableFlag)
                .eq(Article::getId, articleId)
                .eq(loginUser.getRoleWeight() > 300, Article::getUserId, loginUser.getUserId())
                .eq(Article::getRecycleFlag, false)
                .eq(Article::getDeletedFlag, false));
        if(Objects.isNull(article))
            return new ArticleBackDTO();
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
            .select(Tag::getId, Tag::getTagName)
            .eq(Objects.isNull(userId), Tag::getUserId, loginUser.getUserId())
            .eq(Objects.nonNull(userId), Tag::getUserId, userId));
        List<LabelDTO> tagDTOList = tagList.stream()
                .map(e -> LabelDTO.builder()
                        .id(e.getId())
                        .label(e.getTagName())
                        .build())
                .collect(Collectors.toList());
        List<Category> categoryList = categoryMapper.selectList(new LambdaQueryWrapper<Category>()
                .select(Category::getId, Category::getCategoryName)
                .eq(Objects.isNull(userId), Category::getUserId, loginUser.getUserId())
                .eq(Objects.nonNull(userId), Category::getUserId, userId));
        List<LabelDTO> categoryDTOList = categoryList.stream()
                .map(e -> LabelDTO.builder()
                        .id(e.getId())
                        .label(e.getCategoryName())
                        .build())
                .collect(Collectors.toList());
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
        Article article = BeanCopyUtil.copyObject(articleBackVO, Article.class);
        if (Objects.isNull(article.getId())) {
            if (StringUtils.isBlank(article.getArticleTitle()) || StringUtils.isBlank(article.getArticleContent()))
                throw new OperationStatusException("文章标题或者内容不允许为空!");
            article.setUserId(loginUser.getUserId());
            article.setIpAddress(IpUtil.getIpAddress(request));
            article.setIpSource(IpUtil.getIpSource(article.getIpAddress()));
            article.setCreateUser(loginUser.getUserId());
            article.setCreateTime(new Date());
            if (Objects.nonNull(article.getDraftFlag()) && !article.getDraftFlag()) {
                if (Objects.isNull(article.getCategoryId()))
                    throw new OperationStatusException("文章分类不允许为空!");
                Integer count = categoryMapper.selectCount(new LambdaQueryWrapper<Category>()
                        .eq(Category::getId, article.getCategoryId())
                        .eq(Category::getUserId, loginUser.getUserId()));
                if (count != 1)
                    throw new IllegalRequestException();
                article.setPublishUser(loginUser.getUserId());
                article.setPublishTime(new Date());
                if (StringUtils.isBlank(article.getArticleCover()))
                    article.setArticleCover(STATIC_RESOURCE_URL + loginUser.getUserId() + "/" + IMG_ARTICLE.getPath() + "/default/defaultCover.jpg");
            }
            articleMapper.insert(article);
        } else {
            if ((Objects.nonNull(article.getArticleTitle()) && StringUtils.isBlank(article.getArticleTitle())) || (Objects.nonNull(article.getArticleContent()) && StringUtils.isBlank(article.getArticleContent())))
                throw new OperationStatusException("文章标题或者内容不允许为空!");
            Article articleOrigin = articleMapper.selectOne(new LambdaQueryWrapper<Article>()
                    .select(Article::getId, Article::getUserId, Article::getPublishUser, Article::getArticleCover)
                    .eq(Article::getId, article.getId())
                    .eq(loginUser.getRoleWeight() > 100, Article::getDeletedFlag, false)
                    .eq(loginUser.getRoleWeight() > 300, Article::getUserId, loginUser.getUserId()));
            if (Objects.isNull(articleOrigin.getId()))
                throw new IllegalRequestException();
            if (Objects.nonNull(article.getDraftFlag()) && !article.getDraftFlag()) {
                if (Objects.nonNull(article.getCategoryId())) {
                    Integer count = categoryMapper.selectCount(new LambdaQueryWrapper<Category>()
                            .eq(Category::getId, article.getCategoryId())
                            .eq(Category::getUserId, articleOrigin.getUserId()));
                    if (count != 1)
                        throw new IllegalRequestException();
                }
                article.setUpdateUser(loginUser.getUserId());
                article.setUpdateTime(new Date());
                if (Objects.isNull(articleOrigin.getPublishUser())) {
                    article.setPublishUser(loginUser.getUserId());
                    article.setPublishTime(new Date());
                }
                if (Objects.nonNull(article.getArticleCover())) {
                    if (StringUtils.isBlank(article.getArticleCover()))
                        article.setArticleCover(STATIC_RESOURCE_URL + "/" + articleOrigin.getUserId() + IMG_ARTICLE.getPath() + "/default/defaultCover.jpg");
                    multiFileService.updateArticleImgByUrl(articleOrigin.getArticleCover());
                }
            }
            articleTagMapper.update(null, new LambdaUpdateWrapper<ArticleTag>()
                    .set(ArticleTag::getDeletedFlag, true)
                    .eq(ArticleTag::getArticleId, articleOrigin.getId()));
            article.setUserId(articleOrigin.getUserId());
            articleMapper.updateById(article);
        }
        if (CollectionUtils.isNotEmpty(articleBackVO.getTagIdList())) {
            List<Tag> tagList = tagMapper.selectList(new LambdaQueryWrapper<Tag>()
                    .select(Tag::getId)
                    .eq(Tag::getUserId, article.getUserId()));
            if (!tagList.stream().map(Tag::getId).collect(Collectors.toList()).containsAll(articleBackVO.getTagIdList()))
                throw new IllegalRequestException();
            List<ArticleTag> articleTagList = articleBackVO.getTagIdList().stream().map(tagId -> ArticleTag.builder()
                    .tagId(tagId)
                    .articleId(article.getId())
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
        if (Objects.nonNull(condition.getKeywords()))
            condition.setKeywords(condition.getKeywords().trim());
        Integer count = articleMapper.selectArticlesBackDTOCount(condition, loginUser.getUserId(), loginUser.getRoleWeight());
        if (count == 0)
            return new PagePojo<>();
        condition.setCurrent((condition.getCurrent() - 1) * condition.getSize());
        List<ArticlesBackDTO> articlesBackDTOList = articleMapper.listArticlesBackDTO(condition, loginUser.getUserId(), loginUser.getRoleWeight());
        if (articlesBackDTOList.size() == 0)
            return new PagePojo<>(count, new ArrayList<>());
        Map<String, Integer> viewCountMap = redisTemplate.boundHashOps(ARTICLE_VIEW_COUNT).entries();
        Map<String, Integer> likeCountMap = redisTemplate.boundHashOps(ARTICLE_LIKE_COUNT).entries();
        articlesBackDTOList.forEach(item -> {
            item.setViewCount(Objects.requireNonNull(viewCountMap).get(item.getId().toString()));
            item.setLikeCount(Objects.requireNonNull(likeCountMap).get(item.getId().toString()));
        });
        return new PagePojo<>(count, articlesBackDTOList);
    }

    @Override
    @Transactional
    public void updateArticlesStatus(UpdateBatchVO updateBatchVO) {
        LoginUser loginUser = UserUtil.getLoginUser();
        Integer count = articleMapper.updateArticlesStatus(updateBatchVO, loginUser.getUserId(), loginUser.getRoleWeight());
        if (count != updateBatchVO.getIdList().size())
            throw new IllegalRequestException();
        // TODO: 删除文章后更新目录
        if (updateBatchVO.getDeletedFlag()) {
            articleTagMapper.update(null, new LambdaUpdateWrapper<ArticleTag>()
                    .set(ArticleTag::getDeletedFlag, true)
                    .in(ArticleTag::getArticleId, updateBatchVO.getIdList()));

        }
    }

    @Override
    @Transactional
    public void deleteArticleIdList(List<Integer> articleIdList) {
        if (CollectionUtils.isEmpty(articleIdList))
            throw new IllegalRequestException();
        int count = articleMapper.deleteBatchIds(articleIdList);
        if (count != articleIdList.size())
            throw new IllegalRequestException();
        articleTagMapper.delete(new LambdaQueryWrapper<ArticleTag>()
                .in(ArticleTag::getArticleId, articleIdList));
    }

    @Override
    @Transactional
    public void updateArticleStatusVO(CommonStatusVO commonStatusVO) {
        LoginUser loginUser = UserUtil.getLoginUser();
        int count = articleMapper.update(null, new LambdaUpdateWrapper<Article>()
                .set(Article::getTopFlag, commonStatusVO.getTopFlag())
                .set(Article::getPublicFlag, commonStatusVO.getPublicFlag())
                .set(Article::getHiddenFlag, commonStatusVO.getHiddenFlag())
                .set(Article::getCommentableFlag, commonStatusVO.getCommentableFlag())
                .eq(Article::getId, commonStatusVO.getId())
                .eq(loginUser.getRoleWeight() > 300, Article::getUserId, loginUser.getUserId()));
        if (count != 1)
            throw new IllegalRequestException();
    }
}




