package com.iksling.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iksling.blog.dto.*;
import com.iksling.blog.entity.*;
import com.iksling.blog.exception.FileStatusException;
import com.iksling.blog.exception.IllegalRequestException;
import com.iksling.blog.exception.OperationStatusException;
import com.iksling.blog.exception.ServerStatusException;
import com.iksling.blog.mapper.*;
import com.iksling.blog.pojo.Condition;
import com.iksling.blog.pojo.Dict;
import com.iksling.blog.pojo.LoginUser;
import com.iksling.blog.pojo.PagePojo;
import com.iksling.blog.service.ArticleService;
import com.iksling.blog.service.ArticleTagService;
import com.iksling.blog.util.*;
import com.iksling.blog.vo.ArticleBackVO;
import com.iksling.blog.vo.ArticleImageBackVO;
import com.iksling.blog.vo.StatusBackVO;
import com.iksling.blog.vo.TokenBackVO;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;
import java.util.stream.Collectors;

import static com.iksling.blog.constant.CommonConst.STATIC_RESOURCE_URL;
import static com.iksling.blog.constant.FlagConst.*;
import static com.iksling.blog.constant.RedisConst.*;
import static com.iksling.blog.enums.FileDirEnum.IMAGE_ARTICLE;
import static com.iksling.blog.util.CommonUtil.getSplitStringByIndex;

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
    private MultiFileMapper multiFileMapper;

    @Autowired
    private ArticleTagService articleTagService;

    @Autowired
    private RedisTemplate redisTemplate;
    @Resource
    private HttpServletRequest request;
    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer saveOrUpdateArticleBackVO(ArticleBackVO articleBackVO) {
        LoginUser loginUser =  UserUtil.getLoginUser();
        Integer loginUserId = loginUser.getUserId();
        Date dateTime = new Date();
        Article article = BeanCopyUtil.copyObject(articleBackVO, Article.class);
        if (article.getId() == null) {
            if (article.getArticleTitle() == null || article.getArticleContent() == null)
                throw new OperationStatusException();
            if (article.getDraftFlag() == Boolean.FALSE) {
                if (article.getCategoryId() == null)
                    throw new OperationStatusException();
                article.setPublishUser(loginUserId);
                article.setPublishTime(dateTime);
            }
            if (article.getCategoryId() != null) {
                Integer count = categoryMapper.selectCount(new LambdaQueryWrapper<Category>()
                        .eq(Category::getId, article.getCategoryId())
                        .eq(Category::getUserId, loginUserId)
                        .eq(Category::getDeletedFlag, false));
                if (count != 1)
                    throw new OperationStatusException();
            }
            article.setUserId(loginUserId);
            article.setIpAddress(IpUtil.getIpAddress(request));
            article.setIpSource(IpUtil.getIpSource(article.getIpAddress()));
            article.setCreateUser(loginUserId);
            article.setCreateTime(dateTime);
            if (CommonUtil.isNotEmpty(article.getArticleCover()) && !article.getArticleCover().startsWith(STATIC_RESOURCE_URL))
                article.setArticleCover(null);
            articleMapper.insert(article);
            saveArticleDirById(article.getId(), loginUserId, dateTime);
        } else {
            Article articleOrigin = articleMapper.selectOne(new LambdaQueryWrapper<Article>()
                    .select(Article::getUserId, Article::getPublishUser, Article::getArticleCover)
                    .eq(Article::getId, article.getId())
                    .eq(Article::getDeletedFlag, false)
                    .eq(Article::getRecycleFlag, false)
                    .eq(loginUser.getRoleWeight() > 300, Article::getUserId, loginUserId));
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
                if (!articleOrigin.getArticleCover().equals(""))
                    updateArticleImageBy(loginUserId, article.getId(), articleOrigin.getArticleCover().split(STATIC_RESOURCE_URL)[1], dateTime);
            }
            if (articleBackVO.getTagIdList() != null) {
                articleTagMapper.update(null, new LambdaUpdateWrapper<ArticleTag>()
                        .set(ArticleTag::getDeletedFlag, true)
                        .eq(ArticleTag::getDeletedFlag, false)
                        .eq(ArticleTag::getArticleId, article.getId()));
            }
            if (article.getDraftFlag() != null && articleOrigin.getPublishUser() == null) {
                article.setPublishUser(loginUserId);
                article.setPublishTime(dateTime);
            }
            article.setUpdateUser(loginUserId);
            article.setUpdateTime(dateTime);
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
    public String saveArticleImageBackVO(ArticleImageBackVO articleImageBackVO) {
        MultipartFile file = articleImageBackVO.getFile();
        articleImageBackVO.setFile(null);
        LoginUser loginUser = UserUtil.getLoginUser();
        Integer articleUserId = articleImageBackVO.getUserId();
        if (articleUserId == null)
            articleUserId = loginUser.getUserId();
        else if (loginUser.getRoleWeight() > 300 && !loginUser.getUserId().equals(articleUserId))
            throw new OperationStatusException();
        MultiFileUtil.checkValidFile(file, IMAGE_ARTICLE, true);
        Integer articleId = articleImageBackVO.getArticleId();
        MultiFile multiFile = multiFileMapper.selectOne(new LambdaQueryWrapper<MultiFile>()
                .select(MultiFile::getId, MultiFile::getFileFullPath)
                .eq(MultiFile::getFileName, articleId)
                .eq(MultiFile::getUserId, articleUserId)
                .eq(MultiFile::getDeletedCount, 0));
        if (multiFile == null)
            throw new OperationStatusException();
        long fileName = IdWorker.getId();
        String[] originalFilenameArr = file.getOriginalFilename().split("\\.");
        String targetAddr = multiFile.getFileFullPath();
        String fullFileName = fileName + "." + originalFilenameArr[1];
        if (MultiFileUtil.upload(file, targetAddr, fullFileName) == null)
            throw new FileStatusException("文件上传失败!");
        String iPAddress = IpUtil.getIpAddress(request);
        multiFileMapper.insert(MultiFile.builder()
                .userId(articleUserId)
                .parentId(multiFile.getId())
                .fileDesc("{'userId':"+articleUserId+",'articleId':"+articleId+"}")
                .fileMark(IMAGE_ARTICLE.getCurrentPath().intValue())
                .fileName(fileName)
                .fileSize(file.getSize())
                .fileFullPath(targetAddr + "/" + fullFileName)
                .fileExtension(originalFilenameArr[1])
                .fileNameOrigin(originalFilenameArr[0])
                .deletableFlag(false)
                .ipSource(IpUtil.getIpSource(iPAddress))
                .ipAddress(iPAddress)
                .createUser(loginUser.getUserId())
                .createTime(new Date())
                .build());
        return STATIC_RESOURCE_URL + articleUserId + "/" + IMAGE_ARTICLE.getPath() + "/" + articleId + "/" + fullFileName;
    }

    @Override
    @Transactional
    public void saveOrUpdateArticleTokenBackVO(TokenBackVO tokenBackVO) {
        LoginUser loginUser = UserUtil.getLoginUser();
        BoundHashOperations boundHashOperations = redisTemplate.boundHashOps(ARTICLE_TOKEN + "_" + tokenBackVO.getId());
        Map<String, Object> map = boundHashOperations.entries();
        if (map.isEmpty()) {
            if (tokenBackVO.getAccessToken() == null)
                throw new OperationStatusException();
            List<Object> objectList = articleMapper.selectObjs(new LambdaQueryWrapper<Article>()
                    .select(Article::getUserId)
                    .eq(Article::getId, tokenBackVO.getId())
                    .eq(Article::getPublicFlag, false)
                    .eq(Article::getDraftFlag, false)
                    .eq(loginUser.getRoleWeight() > 300, Article::getUserId, loginUser.getUserId()));
            if (objectList.isEmpty())
                throw new OperationStatusException();
            map = new HashMap<>();
            map.put("accessToken", tokenBackVO.getAccessToken());
            map.put("effectiveCount", tokenBackVO.getEffectiveCount() == null ? -1 : tokenBackVO.getEffectiveCount());
            map.put("userId", objectList.get(0));
        } else {
            if (loginUser.getRoleWeight() > 300 && !loginUser.getUserId().equals(map.get("userId")))
                throw new OperationStatusException();
            if (tokenBackVO.getAccessToken() != null)
                map.put("accessToken", tokenBackVO.getAccessToken());
            if (tokenBackVO.getEffectiveCount() != null)
                map.put("effectiveCount", tokenBackVO.getEffectiveCount());
        }
        if (tokenBackVO.getExpireTime() == null) {
            boundHashOperations.persist();
            map.put("expireTime", null);
        } else {
            boundHashOperations.expireAt(tokenBackVO.getExpireTime());
            map.put("expireTime", tokenBackVO.getExpireTime());
        }
        boundHashOperations.putAll(map);
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
        deleteArticleDirByIdList(idList);
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
        if (count != statusBackVO.getIdList().size())
            throw new OperationStatusException();
    }

    @Override
    @Transactional
    public void updateArticlesStatusBackVO(StatusBackVO statusBackVO) {
        LoginUser loginUser = UserUtil.getLoginUser();
        Date updateTime = new Date();
        LambdaUpdateWrapper<Article> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        if (RECYCLE.equals(statusBackVO.getType())) {
            if (statusBackVO.getStatus() == Boolean.TRUE)
                lambdaUpdateWrapper.set(Article::getRecycleFlag, false);
            else
                lambdaUpdateWrapper.set(Article::getDeletedFlag, true);
        } else if (DELETED.equals(statusBackVO.getType())) {
            if (loginUser.getRoleWeight() > 100)
                throw new IllegalRequestException();
            lambdaUpdateWrapper.set(Article::getDeletedFlag, false);
        } else
            lambdaUpdateWrapper.set(Article::getDraftFlag, true).set(Article::getRecycleFlag, true);
        int count = articleMapper.update(null, lambdaUpdateWrapper
                .eq(loginUser.getRoleWeight() > 100, Article::getDeletedFlag, false)
                .eq(loginUser.getRoleWeight() > 300, Article::getUserId, loginUser.getUserId())
                .in(Article::getId, statusBackVO.getIdList())
                .set(Article::getUpdateUser, loginUser.getUserId())
                .set(Article::getUpdateTime, updateTime));
        if (count != statusBackVO.getIdList().size())
            throw new OperationStatusException();
        if (RECYCLE.equals(statusBackVO.getType())) {
            if (statusBackVO.getStatus() == Boolean.TRUE)
                updateArticleDirByIdList(statusBackVO.getIdList(), loginUser.getUserId(), updateTime, false);
            else {
                articleTagMapper.update(null, new LambdaUpdateWrapper<ArticleTag>()
                        .set(ArticleTag::getDeletedFlag, true)
                        .eq(ArticleTag::getDeletedFlag, false)
                        .in(ArticleTag::getArticleId, statusBackVO.getIdList()));
                updateArticleDirByIdList(statusBackVO.getIdList(), loginUser.getUserId(), updateTime, true);
            }
        }
    }

    @Override
    @Transactional
    public void updateBackArticleImagesByFileNameList(List<Long> fileNameList) {
        if (fileNameList.isEmpty())
            throw new OperationStatusException();
        LoginUser loginUser = UserUtil.getLoginUser();
        List<MultiFile> multiFileList = multiFileMapper.selectList(new LambdaQueryWrapper<MultiFile>()
                .select(MultiFile::getId, MultiFile::getFileFullPath, MultiFile::getFileExtension)
                .in(MultiFile::getFileName, fileNameList)
                .eq(MultiFile::getFileMark, IMAGE_ARTICLE.getCurrentPath())
                .eq(MultiFile::getDeletedCount, 0)
                .eq(loginUser.getRoleWeight() > 300, MultiFile::getUserId, loginUser.getUserId()));
        if (multiFileList.size() != fileNameList.size())
            throw new OperationStatusException();
        multiFileList.forEach(e -> {
            long fileNameNew = IdWorker.getId();
            String fileFullPath = e.getFileFullPath();
            String fileFullPathOld = getSplitStringByIndex(fileFullPath, "[_.]", 0);
            String fileFullPathNew = fileFullPathOld + "_" + fileNameNew + "_" + DELETED + "." + e.getFileExtension();
            multiFileMapper.update(null, new LambdaUpdateWrapper<MultiFile>()
                    .set(MultiFile::getFileNameNew, fileNameNew)
                    .set(MultiFile::getFileFullPath, fileFullPathNew)
                    .set(MultiFile::getDeletedCount, 1)
                    .set(MultiFile::getUpdateUser, loginUser.getUserId())
                    .set(MultiFile::getUpdateTime, new Date())
                    .eq(MultiFile::getId, e.getId()));
            MultiFileUtil.rename(fileFullPath, fileFullPathNew);
        });
    }

    @Override
    public ArticleBackDTO getArticleBackDTOById(Integer id) {
        LoginUser loginUser = UserUtil.getLoginUser();
        return articleMapper.selectArticleBackDTOById(id, loginUser.getUserId(), loginUser.getRoleWeight());
    }

    @Override
    public ArticleOptionBackDTO getArticleOptionBackDTO(Integer userId) {
        LoginUser loginUser = UserUtil.getLoginUser();
        if (userId == null || loginUser.getRoleWeight() > 300)
            userId = loginUser.getUserId();
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
                .build();
    }

    @Override
    public PagePojo<ArticlesBackDTO> getArticlesBackDTO(Condition condition) {
        LoginUser loginUser = UserUtil.getLoginUser();
        if (DELETED.equals(condition.getType()) && loginUser.getRoleWeight() > 100)
            return new PagePojo<>();
        Integer count = articleMapper.selectArticlesBackDTOCount(condition, loginUser.getUserId(), loginUser.getRoleWeight());
        if (count == 0)
            return new PagePojo<>();
        condition.setCurrent((condition.getCurrent() - 1) * condition.getSize());
        List<ArticlesBackDTO> articlesBackDTOList = articleMapper.selectArticlesBackDTO(condition, loginUser.getUserId(), loginUser.getRoleWeight());
        if (articlesBackDTOList.isEmpty())
            return new PagePojo<>(count, new ArrayList<>());
        Map<String, Integer> viewCountMap = redisTemplate.boundHashOps(ARTICLE_VIEW_COUNT).entries();
        Map<String, Integer> likeCountMap = redisTemplate.boundHashOps(ARTICLE_LIKE_COUNT).entries();
        articlesBackDTOList.forEach(e -> {
            e.setViewCount(viewCountMap.get(e.getId().toString()));
            e.setLikeCount(likeCountMap.get(e.getId().toString()));
        });
        return new PagePojo<>(count, articlesBackDTOList);
    }

    @Override
    public Dict getArticleTokenById(Integer id) {
        LoginUser loginUser = UserUtil.getLoginUser();
        BoundHashOperations boundHashOperations = redisTemplate.boundHashOps(ARTICLE_TOKEN + "_" + id);
        Map<String, Object> map = boundHashOperations.entries();
        if (map.isEmpty() || (loginUser.getRoleWeight() > 300 && !loginUser.getUserId().equals(map.get("userId"))))
            return Dict.create();
        return Dict.create().putAll(new HashMap<>(map));
    }

    @Override
    @Transactional
    public void saveArticleLike(Integer id) {
        LoginUser loginUser = UserUtil.getLoginUser();
        Integer loginUserId = loginUser.getUserId();
        Integer count = articleMapper.selectCount(new LambdaQueryWrapper<Article>()
                .eq(Article::getId, id)
                .eq(Article::getDraftFlag, false)
                .eq(loginUser.getRoleWeight() > 300, Article::getHiddenFlag, false));
        if (count == 0)
            throw new OperationStatusException();
        HashSet<Integer> articleLikeSet = (HashSet<Integer>) redisTemplate.boundHashOps(ARTICLE_USER_LIKE).get(loginUserId.toString());
        if (articleLikeSet == null)
            articleLikeSet = new HashSet<>();
        if (articleLikeSet.contains(id)) {
            articleLikeSet.remove(id);
            redisTemplate.boundHashOps(ARTICLE_LIKE_COUNT).increment(id.toString(), -1);
        } else {
            articleLikeSet.add(id);
            redisTemplate.boundHashOps(ARTICLE_LIKE_COUNT).increment(id.toString(), 1);
        }
        redisTemplate.boundHashOps(ARTICLE_USER_LIKE).put(loginUserId.toString(), articleLikeSet);
    }

    @Override
    public List<ArticlesDTO> getArticlesDTO(Condition condition) {
        LoginUser loginUser = UserUtil.getLoginUser();
        Integer loginUserId = loginUser.getUserId();
        condition.setUserId(Integer.valueOf(request.getHeader("Blogger-Id")));
        condition.setCurrent((condition.getCurrent() - 1) * condition.getSize());
        condition.setFlag(loginUser.getRoleWeight() > 300 && !loginUserId.equals(condition.getUserId()));
        return articleMapper.selectArticlesDTO(condition);
    }

    @Override
    public ArticleDTO getArticleDTOById(Integer id) {
        LoginUser loginUser = UserUtil.getLoginUser();
        Integer loginUserId = loginUser.getUserId();
        Integer bloggerId = Integer.valueOf(request.getHeader("Blogger-Id"));
        boolean flag = false;
        boolean loginFlag = loginUserId == -1;
        if (!loginFlag)
            flag = loginUser.getRoleWeight() > 300 && !loginUserId.equals(bloggerId);
        ArticleDTO articleDTO = articleMapper.selectArticleDTOById(id, bloggerId, flag, loginFlag);
        if (articleDTO == null)
            return null;
        if (!articleDTO.getPublicFlag()) {
            if (loginFlag)
                articleDTO.setPermitFlag(false);
            else if (flag) {
                HashSet<Integer> articleTokenSet = (HashSet<Integer>) redisTemplate.boundHashOps(ARTICLE_TOKEN).get(loginUserId.toString());
                if (articleTokenSet == null || !articleTokenSet.contains(id)) {
                    articleDTO.setPermitFlag(false);
                    articleDTO.setArticleContent("");
                }
            }
        }
        updateArticleViewCount(id.toString());
        List<ArticlesPaginationDTO> articlesPaginationDTOList = articleMapper.selectArticlesPaginationDTOById(id, bloggerId, flag);
        List<ArticlesRecommendDTO> articlesRecommendDTOList = articleMapper.selectArticlesRecommendDTOById(id, bloggerId, flag);
        if (articlesPaginationDTOList.size() == 2) {
            articleDTO.setLastArticle(articlesPaginationDTOList.get(0));
            articleDTO.setNextArticle(articlesPaginationDTOList.get(1));
        } else if (articlesPaginationDTOList.size() == 1) {
            if (articlesPaginationDTOList.get(0).getId() < id)
                articleDTO.setLastArticle(articlesPaginationDTOList.get(0));
            else
                articleDTO.setNextArticle(articlesPaginationDTOList.get(0));
        }
        articleDTO.setArticlesRecommendDTOList(articlesRecommendDTOList);
        articleDTO.setViewCount((Integer) redisTemplate.boundHashOps(ARTICLE_VIEW_COUNT).get(id.toString()));
        articleDTO.setLikeCount((Integer) redisTemplate.boundHashOps(ARTICLE_LIKE_COUNT).get(id.toString()));
        return articleDTO;
    }

    @Override
    public List<ArticlesRecommendDTO> getArticlesRecommendDTO() {
        LoginUser loginUser = UserUtil.getLoginUser();
        Integer bloggerId = Integer.valueOf(request.getHeader("Blogger-Id"));
        boolean flag = loginUser.getRoleWeight() > 300 && !loginUser.getUserId().equals(bloggerId);
        return BeanCopyUtil.copyList(articleMapper.selectList(new LambdaQueryWrapper<Article>()
                .select(Article::getId, Article::getUserId, Article::getArticleTitle, Article::getArticleCover, Article::getPublicFlag, Article::getPublishTime)
                .eq(Article::getDraftFlag, false)
                .eq(flag, Article::getHiddenFlag, false)
                .eq(Article::getUserId, bloggerId)
                .orderByDesc(Article::getId)
                .last("limit 5")), ArticlesRecommendDTO.class);
    }

    @Override
    public Dict getArticlesPreviewDTO(Condition condition) {
        if (condition.getCategoryId() == null)
            throw new OperationStatusException();
        LoginUser loginUser = UserUtil.getLoginUser();
        condition.setCurrent((condition.getCurrent() - 1) * condition.getSize());
        condition.setFlag(loginUser.getRoleWeight() > 300 && !loginUser.getUserId().equals(condition.getUserId()));
        Dict dict = Dict.create();
        if (condition.getType() != null) {
            List<Object> objectList = categoryMapper.selectObjs(new LambdaQueryWrapper<Category>()
                    .select(Category::getCategoryName)
                    .eq(Category::getId, condition.getCategoryId())
                    .eq(Category::getUserId, Integer.valueOf(request.getHeader("Blogger-Id")))
                    .eq(Category::getDeletedFlag, false)
                    .and(condition.getFlag(), e -> e.eq(Category::getPublicFlag, true).eq(Category::getHiddenFlag, false)));
            if (objectList.isEmpty())
                return dict.set("articlesPreviewDTOList", new ArrayList<>());
            List<ArticlesPreviewDTO> articlesPreviewDTOList = articleMapper.selectArticlesPreviewDTOByCategoryId(condition);
            return dict.set("name", objectList.get(0)).set("articlesPreviewDTOList", articlesPreviewDTOList);
        } else {
            List<Object> objectList = tagMapper.selectObjs(new LambdaQueryWrapper<Tag>()
                    .select(Tag::getTagName)
                    .eq(Tag::getId, condition.getCategoryId())
                    .eq(Tag::getDeletedFlag, false)
                    .eq(Tag::getUserId, Integer.valueOf(request.getHeader("Blogger-Id"))));
            if (objectList.isEmpty())
                return dict.set("articlesPreviewDTOList", new ArrayList<>());
            List<ArticlesPreviewDTO> articlesPreviewDTOList = articleMapper.selectArticlesPreviewDTOByTagId(condition);
            return dict.set("name", objectList.get(0)).set("articlesPreviewDTOList", articlesPreviewDTOList);
        }
    }

    @Override
    public PagePojo<ArticlesArchiveDTO> getArticlesArchiveDTO(Condition condition) {
        LoginUser loginUser = UserUtil.getLoginUser();
        Integer bloggerId = Integer.valueOf(request.getHeader("Blogger-Id"));
        Page<Article> page = new Page<>(condition.getCurrent(), condition.getSize());
        Page<Article> articlePage = articleMapper.selectPage(page, new LambdaQueryWrapper<Article>()
                .select(Article::getId, Article::getUserId, Article::getArticleTitle, Article::getPublicFlag, Article::getHiddenFlag, Article::getPublishTime)
                .orderByDesc(Article::getId)
                .eq(Article::getUserId, bloggerId)
                .eq(Article::getDraftFlag, false)
                .and(loginUser.getRoleWeight() > 300 && !loginUser.getUserId().equals(bloggerId), e -> e.eq(Article::getPublicFlag, true).eq(Article::getHiddenFlag, false)));
        List<ArticlesArchiveDTO> articlesArchiveDTOList = BeanCopyUtil.copyList(articlePage.getRecords(), ArticlesArchiveDTO.class);
        return new PagePojo<>((int) articlePage.getTotal(), articlesArchiveDTOList);
    }

    @Override
    public List<ArticlesSearchDTO> getArticlesSearchDTO(Condition condition) {
        if (CommonUtil.isEmpty(condition.getKeywords()))
            return new ArrayList<>();
        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must(QueryBuilders.boolQuery()
                .should(QueryBuilders.matchQuery("articleTitle", condition.getKeywords()))
                .should(QueryBuilders.matchQuery("articleContent", condition.getKeywords())))
                .must(QueryBuilders.termQuery("draftFlag", false))
                .must(QueryBuilders.termQuery("publicFlag", true))
                .must(QueryBuilders.termQuery("hiddenFlag", false));
        if (condition.getStatus() == null)
            boolQueryBuilder.must(QueryBuilders.termQuery("userId", request.getHeader("Blogger-Id")));
        nativeSearchQueryBuilder.withQuery(boolQueryBuilder);
        HighlightBuilder.Field titleField = new HighlightBuilder.Field("articleTitle");
        titleField.preTags("<span style='color:#f47466'>");
        titleField.postTags("</span>");
        HighlightBuilder.Field contentField = new HighlightBuilder.Field("articleContent");
        contentField.preTags("<span style='color:#f47466'>");
        contentField.postTags("</span>");
        contentField.fragmentSize(200);
        nativeSearchQueryBuilder.withHighlightFields(titleField, contentField);
        SearchHits<ArticlesSearchDTO> search;
        try {
            search = elasticsearchRestTemplate.search(nativeSearchQueryBuilder.build(), ArticlesSearchDTO.class);
        } catch (Exception e) {
            throw new ServerStatusException("查询超时!");
        }
        return search.getSearchHits().stream().map(hit -> {
            ArticlesSearchDTO article = hit.getContent();
            List<String> titleHighLightList = hit.getHighlightFields().get("articleTitle");
            if (CommonUtil.isNotEmpty(titleHighLightList))
                article.setArticleTitle(titleHighLightList.get(0));
            List<String> contentHighLightList = hit.getHighlightFields().get("articleContent");
            if (CommonUtil.isNotEmpty(contentHighLightList))
                article.setArticleContent(contentHighLightList.get(0));
            return article;
        }).collect(Collectors.toList());
    }

    private void updateArticleImageBy(Integer loginUserId, Integer articleId, String fileFullPath, Date updateTime) {
        long fileNameNew = IdWorker.getId();
        String[] pathArr = fileFullPath.split("[_.]");
        String fileFullPathOld = pathArr[0];
        String fileFullPathNew = fileFullPathOld + "_" + fileNameNew + "_" + DELETED + "." + pathArr[pathArr.length - 1];
        multiFileMapper.update(null, new LambdaUpdateWrapper<MultiFile>()
                .set(MultiFile::getFileNameNew, fileNameNew)
                .set(MultiFile::getFileFullPath, fileFullPathNew)
                .set(MultiFile::getDeletedCount, 1)
                .set(MultiFile::getUpdateUser, loginUserId)
                .set(MultiFile::getUpdateTime, updateTime)
                .eq(MultiFile::getFileName, getSplitStringByIndex(fileFullPathOld, "/", -1))
                .inSql(MultiFile::getParentId, "select mf.id from (select id from tb_multi_file where file_name="+articleId+") mf"));
        MultiFileUtil.rename(fileFullPathOld, fileFullPathNew);
    }

    private void saveArticleDirById(Integer id, Integer loginUserId, Date createTime) {
        List<Object> objectList = multiFileMapper.selectObjs(new LambdaQueryWrapper<MultiFile>()
                .select(MultiFile::getId)
                .eq(MultiFile::getUserId, loginUserId)
                .eq(MultiFile::getFileName, IMAGE_ARTICLE.getCurrentPath()));
        multiFileMapper.insert(MultiFile.builder()
                .userId(loginUserId)
                .parentId((Integer) objectList.get(0))
                .fileDesc("{'articleId':"+id+"}")
                .fileName(Long.valueOf(id))
                .fileFullPath(loginUserId + "/" + IMAGE_ARTICLE.getPath() + "/" + id)
                .fileNameOrigin(id.toString())
                .deletableFlag(false)
                .createUser(loginUserId)
                .createTime(createTime)
                .build());
    }

    private void updateArticleDirByIdList(List<Integer> idList, Integer loginUserId, Date updateTime, boolean deletedFlag) {
        List<MultiFile> multiFileList = multiFileMapper.selectList(new LambdaQueryWrapper<MultiFile>()
                .select(MultiFile::getId, MultiFile::getFileFullPath)
                .in(MultiFile::getFileName, idList));
        if (deletedFlag)
            multiFileList.forEach(e -> {
                long fileNameNew = IdWorker.getId();
                String fileFullPath = e.getFileFullPath();
                String fileFullPathOld = getSplitStringByIndex(fileFullPath, "_", 0);
                String fileFullPathNew = fileFullPathOld + "_" + fileNameNew + "_" + DELETED;
                multiFileMapper.update(null, new LambdaUpdateWrapper<MultiFile>()
                        .set(MultiFile::getFileNameNew, fileNameNew)
                        .set(MultiFile::getFileFullPath, fileFullPathNew)
                        .set(MultiFile::getDeletedCount, 1)
                        .set(MultiFile::getUpdateUser, loginUserId)
                        .set(MultiFile::getUpdateTime, updateTime)
                        .eq(MultiFile::getId, e.getId()));
                multiFileMapper.update(null, new LambdaUpdateWrapper<MultiFile>()
                        .setSql("deleted_count=if(deleted_count>0,deleted_count+1,deleted_count-1),file_full_path=concat('"+fileFullPathNew+"',substring(file_full_path,"+(fileFullPath.length() + 1)+"))")
                        .eq(MultiFile::getParentId, e.getId()));
                MultiFileUtil.rename(fileFullPath, fileFullPathNew);
            });
        else
            multiFileList.forEach(e -> {
                String fileFullPath = e.getFileFullPath();
                String fileFullName = getSplitStringByIndex(fileFullPath, "/", -1);
                String fileFullPathOld = fileFullPath.substring(0, fileFullPath.length() - fileFullName.length()) + getSplitStringByIndex(fileFullName, "[_.]", 0);
                multiFileMapper.update(null, new LambdaUpdateWrapper<MultiFile>()
                        .set(MultiFile::getFileFullPath, fileFullPathOld)
                        .set(MultiFile::getDeletedCount, 0)
                        .set(MultiFile::getPublicFlag, true)
                        .set(MultiFile::getHiddenFlag, false)
                        .set(MultiFile::getUpdateUser, loginUserId)
                        .set(MultiFile::getUpdateTime, updateTime)
                        .eq(MultiFile::getId, e.getId()));
                multiFileMapper.update(null, new LambdaUpdateWrapper<MultiFile>()
                        .setSql("deleted_count=if(deleted_count>0,deleted_count-1,deleted_count+1),file_full_path=concat('"+fileFullPathOld+"',substring(file_full_path,"+(fileFullPath.length() + 1)+"))")
                        .eq(MultiFile::getParentId, e.getId()));
                MultiFileUtil.rename(fileFullPath, fileFullPathOld);
            });
    }

    private void deleteArticleDirByIdList(List<Integer> idList) {
        List<MultiFile> multiFileList = multiFileMapper.selectList(new LambdaQueryWrapper<MultiFile>()
                .select(MultiFile::getId, MultiFile::getFileFullPath)
                .in(MultiFile::getFileName, idList));
        List<Object> objectList = multiFileList.stream()
                .map(MultiFile::getId)
                .collect(Collectors.toList());
        multiFileMapper.delete(new LambdaUpdateWrapper<MultiFile>()
                .in(MultiFile::getId, objectList)
                .or()
                .in(MultiFile::getParentId, objectList));
        multiFileList.forEach(e -> MultiFileUtil.delete(e.getFileFullPath()));
    }

    @Async
    public void updateArticleViewCount(String id) {
        HttpSession session = request.getSession();
        Set<String> articleIdSet = (Set<String>) session.getAttribute("articleIdSet");
        if (articleIdSet == null) {
            articleIdSet = new HashSet<>();
        }
        if (!articleIdSet.contains(id)) {
            articleIdSet.add(id);
            session.setAttribute("articleIdSet", articleIdSet);
            redisTemplate.boundHashOps(ARTICLE_VIEW_COUNT).increment(id, 1);
        }
    }
}




