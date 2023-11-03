package com.iksling.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
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
import com.iksling.blog.vo.ConditionBackVO;
import com.iksling.blog.vo.StatusBackVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

import static com.iksling.blog.constant.CommonConst.STATIC_RESOURCE_URL;
import static com.iksling.blog.constant.FlagConst.*;
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
    @Transactional(rollbackFor = Exception.class)
    public Integer saveOrUpdateArticleBackVO(ArticleBackVO articleBackVO) {
        LoginUser loginUser =  UserUtil.getLoginUser();
        Article article = BeanCopyUtil.copyObject(articleBackVO, Article.class);
        if (article.getId() == null) {
            if (article.getArticleTitle() == null || article.getArticleContent() == null)
                throw new OperationStatusException();
            if (article.getDraftFlag() == Boolean.FALSE) {
                if (article.getCategoryId() == null)
                    throw new OperationStatusException();
                article.setPublishUser(loginUser.getUserId());
                article.setPublishTime(new Date());
            }
            if (article.getCategoryId() != null) {
                Integer count = categoryMapper.selectCount(new LambdaQueryWrapper<Category>()
                        .eq(Category::getId, article.getCategoryId())
                        .eq(Category::getUserId, loginUser.getUserId())
                        .eq(Category::getDeletedFlag, false));
                if (count != 1)
                    throw new OperationStatusException();
            }
            article.setUserId(loginUser.getUserId());
            article.setIpAddress(IpUtil.getIpAddress(request));
            article.setIpSource(IpUtil.getIpSource(article.getIpAddress()));
            article.setCreateUser(loginUser.getUserId());
            article.setCreateTime(new Date());
            if (article.getArticleCover() != null)
                if (!article.getArticleCover().startsWith(STATIC_RESOURCE_URL))
                    article.setArticleCover(null);
            articleMapper.insert(article);
            multiDirService.saveArticleDirById(article.getId());
        } else {
            Article articleOrigin = articleMapper.selectOne(new LambdaQueryWrapper<Article>()
                    .select(Article::getUserId, Article::getPublishUser, Article::getArticleCover)
                    .eq(Article::getId, article.getId())
                    .eq(Article::getDeletedFlag, false)
                    .eq(Article::getRecycleFlag, false)
                    .eq(loginUser.getRoleWeight() > 300, Article::getUserId, loginUser.getUserId()));
            if (articleOrigin == null)
                throw new OperationStatusException();
            if (article.getCategoryId() != null) {
                Integer count = categoryMapper.selectCount(new LambdaQueryWrapper<Category>()
                        .eq(Category::getId, article.getCategoryId())
                        .eq(Category::getUserId, articleOrigin.getUserId())
                        .eq(Category::getDeletedFlag, false));
                if (count != 1)
                    throw new IllegalRequestException();
            }
            if (article.getArticleCover() != null) {
                if (!article.getArticleCover().startsWith(STATIC_RESOURCE_URL))
                    article.setArticleCover("");
                if (articleOrigin.getArticleCover().startsWith(STATIC_RESOURCE_URL + articleOrigin.getUserId() + "/" + IMG_ARTICLE.getPath() + "/" + article.getId()))
                    multiFileService.updateArticleImageBy(articleOrigin.getUserId(), article.getId(), CommonUtil.getSplitStringByIndex(articleOrigin.getArticleCover(), "/", -1));
            }
            if (articleBackVO.getTagIdList() != null) {
                articleTagMapper.update(null, new LambdaUpdateWrapper<ArticleTag>()
                        .set(ArticleTag::getDeletedFlag, true)
                        .eq(ArticleTag::getDeletedFlag, false)
                        .eq(ArticleTag::getArticleId, article.getId()));
            }
            if (article.getDraftFlag() != null && articleOrigin.getPublishUser() == null) {
                article.setPublishUser(loginUser.getUserId());
                article.setPublishTime(new Date());
            }
            article.setUpdateUser(loginUser.getUserId());
            article.setUpdateTime(new Date());
            articleMapper.updateById(article);
            article.setUserId(articleOrigin.getUserId());
        }
        if (CommonUtil.isNotEmpty(articleBackVO.getTagIdList())) {
            List<Tag> tagList = tagMapper.selectList(new LambdaQueryWrapper<Tag>()
                    .select(Tag::getId)
                    .eq(Tag::getUserId, article.getUserId())
                    .eq(Tag::getDeletedFlag, false));
            if (!tagList.stream().map(Tag::getId).collect(Collectors.toList()).containsAll(articleBackVO.getTagIdList()))
                throw new OperationStatusException();
            List<ArticleTag> articleTagList = articleBackVO.getTagIdList().stream().distinct().map(tagId -> ArticleTag.builder()
                    .tagId(tagId)
                    .articleId(article.getId())
                    .build()).collect(Collectors.toList());
            articleTagService.saveBatch(articleTagList);
        }
        return article.getId();
    }

    @Override
    @Transactional
    public void deleteBackArticlesByIdList(List<Integer> idList) {
        if (idList.isEmpty())
            throw new IllegalRequestException();
        int count = articleMapper.delete(new LambdaUpdateWrapper<Article>()
                .eq(Article::getDeletedFlag, true)
                .in(Article::getId, idList));
        if (count != idList.size())
            throw new IllegalRequestException();
        articleTagMapper.delete(new LambdaUpdateWrapper<ArticleTag>()
                .in(ArticleTag::getArticleId, idList));
        multiDirService.deleteArticleDirByIdList(idList);
    }

    @Override
    @Transactional
    public void updateArticleStatusBackVO(StatusBackVO statusBackVO) {
        LoginUser loginUser = UserUtil.getLoginUser();
        LambdaUpdateWrapper<Article> lambdaUpdateWrapper = new LambdaUpdateWrapper<Article>()
                .in(Article::getId, statusBackVO.getIdList())
                .eq(Article::getDraftFlag, false)
                .eq(loginUser.getRoleWeight() > 300, Article::getUserId, loginUser.getUserId());
        if (PUBLIC.equals(statusBackVO.getType()))
            lambdaUpdateWrapper.setSql("public_flag = !public_flag");
        else if (HIDDEN.equals(statusBackVO.getType()))
            lambdaUpdateWrapper.setSql("hidden_flag = !hidden_flag");
        else if (COMMENTABLE.equals(statusBackVO.getType()))
            lambdaUpdateWrapper.setSql("commentable_flag = !commentable_flag");
        else
            lambdaUpdateWrapper.setSql("top_flag = !top_flag");
        int count = articleMapper.update(null, lambdaUpdateWrapper);
        if (count != 1)
            throw new OperationStatusException();
    }

    @Override
    @Transactional
    public void updateArticlesStatusBackVO(StatusBackVO statusBackVO) {
        LoginUser loginUser = UserUtil.getLoginUser();
        LambdaUpdateWrapper<Article> lambdaUpdateWrapper = new LambdaUpdateWrapper<Article>()
                .eq(loginUser.getRoleWeight() > 100, Article::getDeletedFlag, false)
                .eq(loginUser.getRoleWeight() > 300, Article::getUserId, loginUser.getUserId())
                .in(Article::getId, statusBackVO.getIdList());
        if (RECYCLE.equals(statusBackVO.getType())) {
            if (statusBackVO.getStatus() == Boolean.TRUE)
                lambdaUpdateWrapper.set(Article::getRecycleFlag, false);
            else
                lambdaUpdateWrapper.set(Article::getDeletedFlag, true);
        } else if (DELETED.equals(statusBackVO.getType())) {
            if (loginUser.getRoleWeight() > 100)
                throw new IllegalRequestException();
            else
                lambdaUpdateWrapper.set(Article::getDeletedFlag, false);
        } else
            lambdaUpdateWrapper.set(Article::getDraftFlag, true).set(Article::getRecycleFlag, true);
        int count = articleMapper.update(null, lambdaUpdateWrapper);
        if (count != statusBackVO.getIdList().size())
            throw new OperationStatusException();
        if (RECYCLE.equals(statusBackVO.getType()) && !statusBackVO.getStatus() == Boolean.TRUE) {
            articleTagMapper.update(null, new LambdaUpdateWrapper<ArticleTag>()
                    .set(ArticleTag::getDeletedFlag, true)
                    .eq(ArticleTag::getDeletedFlag, false)
                    .in(ArticleTag::getArticleId, statusBackVO.getIdList()));
            multiDirService.updateArticleDirByIdList(statusBackVO.getIdList());
        }
    }

    @Override
    public ArticleBackDTO getArticleBackDTOById(Integer id) {
        LoginUser loginUser = UserUtil.getLoginUser();
        return articleMapper.selectArticleBackDTOById(id, loginUser.getUserId(), loginUser.getRoleWeight());
    }

    @Override
    public ArticleOptionBackDTO getArticleOptionBackDTO(Integer userId) {
        LoginUser loginUser = UserUtil.getLoginUser();
        if (userId == null)
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
                .tagDTOList(tagDTOList)
                .categoryDTOList(categoryDTOList)
                .staticResourceUrl(STATIC_RESOURCE_URL)
                .build();
    }

    @Override
    public PagePojo<ArticlesBackDTO> getArticlesBackDTO(ConditionBackVO condition) {
        LoginUser loginUser = UserUtil.getLoginUser();
        if (DELETED.equals(condition.getType()) && loginUser.getRoleWeight() > 100)
            return new PagePojo<>();
        Integer count = articleMapper.selectArticlesBackDTOCount(condition, loginUser.getUserId(), loginUser.getRoleWeight());
        if (count == 0)
            return new PagePojo<>();
        condition.setCurrent((condition.getCurrent() - 1) * condition.getSize());
        List<ArticlesBackDTO> articlesBackDTOList = articleMapper.selectArticlesBackDTO(condition, loginUser.getUserId(), loginUser.getRoleWeight());
        if (articlesBackDTOList.size() == 0)
            return new PagePojo<>(count, new ArrayList<>());
        Map<String, Integer> viewCountMap = redisTemplate.boundHashOps(ARTICLE_VIEW_COUNT).entries();
        Map<String, Integer> likeCountMap = redisTemplate.boundHashOps(ARTICLE_LIKE_COUNT).entries();
        articlesBackDTOList.forEach(e -> {
            e.setViewCount(Objects.requireNonNull(viewCountMap).get(e.getId().toString()));
            e.setLikeCount(Objects.requireNonNull(likeCountMap).get(e.getId().toString()));
        });
        return new PagePojo<>(count, articlesBackDTOList);
    }
}




