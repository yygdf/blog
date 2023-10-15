package com.iksling.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iksling.blog.dto.ArticleBackDTO;
import com.iksling.blog.dto.ArticleOptionBackDTO;
import com.iksling.blog.dto.ArticlesBackDTO;
import com.iksling.blog.dto.LabelBackDTO;
import com.iksling.blog.entity.Article;
import com.iksling.blog.entity.ArticleTag;
import com.iksling.blog.entity.Category;
import com.iksling.blog.entity.Tag;
import com.iksling.blog.exception.IllegalRequestException;
import com.iksling.blog.exception.OperationStatusException;
import com.iksling.blog.mapper.ArticleMapper;
import com.iksling.blog.mapper.ArticleTagMapper;
import com.iksling.blog.mapper.CategoryMapper;
import com.iksling.blog.mapper.TagMapper;
import com.iksling.blog.pojo.LoginUser;
import com.iksling.blog.pojo.PagePojo;
import com.iksling.blog.service.ArticleService;
import com.iksling.blog.service.ArticleTagService;
import com.iksling.blog.service.MultiDirService;
import com.iksling.blog.service.MultiFileService;
import com.iksling.blog.util.BeanCopyUtil;
import com.iksling.blog.util.CommonUtil;
import com.iksling.blog.util.IpUtil;
import com.iksling.blog.util.UserUtil;
import com.iksling.blog.vo.ArticleBackVO;
import com.iksling.blog.vo.StatusBackVO;
import com.iksling.blog.vo.ConditionBackVO;
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
    private MultiDirService multiDirService;
    @Autowired
    private MultiFileService multiFileService;
    @Autowired
    private ArticleTagService articleTagService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Resource
    private HttpServletRequest request;

    @Override
    public ArticleBackDTO getArticleBackDTOById(Integer id) {
        LoginUser loginUser = UserUtil.getLoginUser();
        Article article = articleMapper.selectOne(new LambdaQueryWrapper<Article>()
                .select(Article::getId, Article::getUserId, Article::getCategoryId, Article::getArticleTitle, Article::getArticleCover, Article::getArticleContent,
                        Article::getTopFlag, Article::getDraftFlag, Article::getPublicFlag, Article::getHiddenFlag, Article::getCommentableFlag)
                .eq(Article::getId, id)
                .eq(Article::getRecycleFlag, false)
                .eq(Article::getDeletedFlag, false)
                .eq(loginUser.getRoleWeight() > 300, Article::getUserId, loginUser.getUserId()));
        if(Objects.isNull(article))
            return new ArticleBackDTO();
        List<Integer> tagIdList = articleTagMapper.selectList(new LambdaQueryWrapper<ArticleTag>()
                .select(ArticleTag::getTagId)
                .eq(ArticleTag::getArticleId, id)
                .eq(ArticleTag::getDeletedFlag, false))
                .stream().map(ArticleTag::getTagId).collect(Collectors.toList());
        ArticleBackDTO articleBackDTO = BeanCopyUtil.copyObject(article, ArticleBackDTO.class);
        articleBackDTO.setTagIdList(tagIdList);
        return articleBackDTO;
    }

    @Override
    public ArticleOptionBackDTO getArticleOptionBackDTO(Integer userId) {
        LoginUser loginUser = UserUtil.getLoginUser();
        if (Objects.isNull(userId))
            userId = loginUser.getUserId();
        else if (loginUser.getRoleWeight() > 300 && !loginUser.getUserId().equals(userId))
            return new ArticleOptionBackDTO();
        List<Tag> tagList = tagMapper.selectList(new LambdaQueryWrapper<Tag>()
            .select(Tag::getId, Tag::getTagName)
            .eq(Tag::getUserId, userId)
            .eq(Tag::getDeletedFlag, false));
        List<LabelBackDTO> tagDTOList = tagList.stream()
                .map(e -> LabelBackDTO.builder()
                        .id(e.getId())
                        .label(e.getTagName())
                        .build())
                .collect(Collectors.toList());
        List<Category> categoryList = categoryMapper.selectList(new LambdaQueryWrapper<Category>()
                .select(Category::getId, Category::getCategoryName)
                .eq(Category::getUserId, userId)
                .eq(Category::getDeletedFlag, false));
        List<LabelBackDTO> categoryDTOList = categoryList.stream()
                .map(e -> LabelBackDTO.builder()
                        .id(e.getId())
                        .label(e.getCategoryName())
                        .build())
                .collect(Collectors.toList());
        return ArticleOptionBackDTO.builder()
                .userId(userId)
                .tagDTOList(tagDTOList)
                .categoryDTOList(categoryDTOList)
                .staticResourceUrl(STATIC_RESOURCE_URL)
                .build();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer saveOrUpdateArticleBackVO(ArticleBackVO articleBackVO) {
        LoginUser loginUser =  UserUtil.getLoginUser();
        Article article = new Article();
        if (Objects.isNull(articleBackVO.getId())) {
            if (StringUtils.isBlank(articleBackVO.getArticleTitle()) || StringUtils.isBlank(articleBackVO.getArticleContent()))
                throw new OperationStatusException("文章标题或者内容不允许为空!");
            article.setArticleTitle(articleBackVO.getArticleTitle());
            article.setArticleContent(articleBackVO.getArticleContent());
            article.setUserId(loginUser.getUserId());
            article.setTopFlag(articleBackVO.getTopFlag());
            article.setPublicFlag(articleBackVO.getPublicFlag());
            article.setHiddenFlag(articleBackVO.getHiddenFlag());
            article.setCommentableFlag(articleBackVO.getCommentableFlag());
            article.setIpAddress(IpUtil.getIpAddress(request));
            article.setIpSource(IpUtil.getIpSource(article.getIpAddress()));
            article.setCreateUser(loginUser.getUserId());
            article.setCreateTime(new Date());
            if (Objects.nonNull(articleBackVO.getDraftFlag()) && !articleBackVO.getDraftFlag()) {
                if (Objects.isNull(articleBackVO.getCategoryId()))
                    throw new OperationStatusException("文章分类不允许为空!");
                article.setDraftFlag(false);
                article.setPublishUser(loginUser.getUserId());
                article.setPublishTime(new Date());
            }
            if (Objects.nonNull(articleBackVO.getCategoryId())) {
                Integer count = categoryMapper.selectCount(new LambdaQueryWrapper<Category>()
                        .eq(Category::getId, articleBackVO.getCategoryId())
                        .eq(Category::getUserId, loginUser.getUserId())
                        .eq(Category::getDeletedFlag, false));
                if (count != 1)
                    throw new IllegalRequestException();
                article.setCategoryId(articleBackVO.getCategoryId());
            }
            if (StringUtils.isBlank(articleBackVO.getArticleCover()) || !articleBackVO.getArticleCover().startsWith(STATIC_RESOURCE_URL))
                article.setArticleCover(null);
            articleMapper.insert(article);
            multiDirService.saveArticleDirById(article.getId());
        } else {
            if (Objects.nonNull(articleBackVO.getArticleTitle())) {
                if (StringUtils.isBlank(articleBackVO.getArticleTitle()))
                    throw new OperationStatusException("文章标题不允许为空!");
                article.setArticleTitle(articleBackVO.getArticleTitle());
            }
            if (Objects.nonNull(articleBackVO.getArticleContent())) {
                if (StringUtils.isBlank(articleBackVO.getArticleContent()))
                    throw new OperationStatusException("文章内容不允许为空!");
                article.setArticleContent(articleBackVO.getArticleContent());
            }
            Article articleOrigin = articleMapper.selectOne(new LambdaQueryWrapper<Article>()
                    .select(Article::getId, Article::getUserId, Article::getPublishUser, Article::getArticleCover)
                    .eq(Article::getId, articleBackVO.getId())
                    .eq(Article::getDeletedFlag, false)
                    .eq(Article::getRecycleFlag, false)
                    .eq(loginUser.getRoleWeight() > 300, Article::getUserId, loginUser.getUserId()));
            if (Objects.isNull(articleOrigin))
                throw new IllegalRequestException();
            if (Objects.nonNull(articleBackVO.getCategoryId())) {
                Integer count = categoryMapper.selectCount(new LambdaQueryWrapper<Category>()
                        .eq(Category::getId, articleBackVO.getCategoryId())
                        .eq(Category::getUserId, articleOrigin.getUserId())
                        .eq(Category::getDeletedFlag, false));
                if (count != 1)
                    throw new IllegalRequestException();
                article.setCategoryId(articleBackVO.getCategoryId());
            }
            if (Objects.nonNull(articleBackVO.getArticleCover())) {
                if (!articleBackVO.getArticleCover().startsWith(STATIC_RESOURCE_URL))
                    article.setArticleCover("");
                else
                    article.setArticleCover(articleBackVO.getArticleCover());
                if (articleOrigin.getArticleCover().startsWith(STATIC_RESOURCE_URL + articleOrigin.getUserId() + "/" + IMG_ARTICLE.getPath() + "/" + articleOrigin.getId()))
                    multiFileService.updateArticleImgBy(articleOrigin.getUserId(), articleOrigin.getId(), CommonUtil.getSplitStringByIndex(articleOrigin.getArticleCover(), "/", -1));
            }
            if (CollectionUtils.isNotEmpty(articleBackVO.getTagIdList())) {
                articleTagMapper.update(null, new LambdaUpdateWrapper<ArticleTag>()
                        .set(ArticleTag::getDeletedFlag, true)
                        .eq(ArticleTag::getDeletedFlag, false)
                        .eq(ArticleTag::getArticleId, articleOrigin.getId()));
            }
            if (Objects.nonNull(articleBackVO.getTopFlag()))
                article.setTopFlag(articleBackVO.getTopFlag());
            if (Objects.nonNull(articleBackVO.getDraftFlag())) {
                article.setDraftFlag(articleBackVO.getDraftFlag());
                if (Objects.isNull(articleOrigin.getPublishUser())) {
                    article.setPublishUser(loginUser.getUserId());
                    article.setPublishTime(new Date());
                }
            }
            if (Objects.nonNull(articleBackVO.getPublicFlag()))
                article.setPublicFlag(articleBackVO.getPublicFlag());
            if (Objects.nonNull(articleBackVO.getHiddenFlag()))
                article.setHiddenFlag(articleBackVO.getHiddenFlag());
            if (Objects.nonNull(articleBackVO.getCommentableFlag()))
                article.setCommentableFlag(articleBackVO.getCommentableFlag());
            article.setId(articleBackVO.getId());
            article.setUpdateUser(loginUser.getUserId());
            article.setUpdateTime(new Date());
            articleMapper.updateById(article);
            article.setUserId(articleOrigin.getUserId());
        }
        if (CollectionUtils.isNotEmpty(articleBackVO.getTagIdList())) {
            List<Tag> tagList = tagMapper.selectList(new LambdaQueryWrapper<Tag>()
                    .select(Tag::getId)
                    .eq(Tag::getUserId, article.getUserId())
                    .eq(Tag::getDeletedFlag, false));
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
    public PagePojo<ArticlesBackDTO> getArticlesBackDTO(ConditionBackVO condition) {
        LoginUser loginUser = UserUtil.getLoginUser();
        if (Objects.equals(condition.getType(), 7) && loginUser.getRoleWeight() > 100)
            return new PagePojo<>();
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
    public void updateArticlesStatusBackVO(StatusBackVO statusBackVO) {
        LoginUser loginUser = UserUtil.getLoginUser();
        LambdaUpdateWrapper<Article> lambdaUpdateWrapper = new LambdaUpdateWrapper<Article>()
                .eq(loginUser.getRoleWeight() > 100, Article::getDeletedFlag, false)
                .eq(loginUser.getRoleWeight() > 300, Article::getUserId, loginUser.getUserId())
                .in(Article::getId, statusBackVO.getIdList());
        if (statusBackVO.getType().equals(5))
            lambdaUpdateWrapper.set(Article::getDraftFlag, true).set(Article::getRecycleFlag, true);
        else if (statusBackVO.getType().equals(6)) {
            if (Objects.equals(statusBackVO.getStatus(), true))
                lambdaUpdateWrapper.set(Article::getRecycleFlag, false);
            else
                lambdaUpdateWrapper.set(Article::getDeletedFlag, true);
        } else if (statusBackVO.getType().equals(7) && loginUser.getRoleWeight() <= 100)
            lambdaUpdateWrapper.set(Article::getDeletedFlag, false);
        else
            throw new OperationStatusException("参数异常!");
        int count = articleMapper.update(null, lambdaUpdateWrapper);
        if (count != statusBackVO.getIdList().size())
            throw new IllegalRequestException();
        if (statusBackVO.getType().equals(6) && !Objects.equals(statusBackVO.getStatus(), true)) {
            articleTagMapper.update(null, new LambdaUpdateWrapper<ArticleTag>()
                    .set(ArticleTag::getDeletedFlag, true)
                    .eq(ArticleTag::getDeletedFlag, false)
                    .in(ArticleTag::getArticleId, statusBackVO.getIdList()));
            multiDirService.updateArticleDirByIdList(statusBackVO.getIdList());
        }
    }

    @Override
    @Transactional
    public void deleteBackArticlesByIdList(List<Integer> idList) {
        if (CollectionUtils.isEmpty(idList))
            throw new IllegalRequestException();
        int count = articleMapper.delete(new LambdaUpdateWrapper<Article>()
                .eq(Article::getDeletedFlag, true)
                .in(Article::getId, idList));
        if (count != idList.size())
            throw new IllegalRequestException();
        articleTagMapper.delete(new LambdaUpdateWrapper<ArticleTag>()
                .in(ArticleTag::getArticleId, idList));
        multiDirService.removeArticleDirByIdList(idList);
    }

    @Override
    @Transactional
    public void updateArticleStatusBackVO(StatusBackVO statusBackVO) {
        LoginUser loginUser = UserUtil.getLoginUser();
        LambdaUpdateWrapper<Article> lambdaUpdateWrapper = new LambdaUpdateWrapper<Article>()
                .in(Article::getId, statusBackVO.getIdList())
                .eq(Article::getDraftFlag, false)
                .eq(loginUser.getRoleWeight() > 300, Article::getUserId, loginUser.getUserId());
        if (statusBackVO.getType().equals(1))
            lambdaUpdateWrapper.setSql("top_flag = !top_flag");
        else if (statusBackVO.getType().equals(2))
            lambdaUpdateWrapper.setSql("public_flag = !public_flag");
        else if (statusBackVO.getType().equals(3))
            lambdaUpdateWrapper.setSql("hidden_flag = !hidden_flag");
        else if (statusBackVO.getType().equals(4))
            lambdaUpdateWrapper.setSql("commentable_flag = !commentable_flag");
        else
            throw new OperationStatusException("参数异常!");
        int count = articleMapper.update(null, lambdaUpdateWrapper);
        if (count != 1)
            throw new IllegalRequestException();
    }
}




