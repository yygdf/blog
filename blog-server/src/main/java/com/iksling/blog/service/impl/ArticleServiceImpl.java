package com.iksling.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iksling.blog.dto.ArticleBackDTO;
import com.iksling.blog.dto.ArticleOptionBackDTO;
import com.iksling.blog.dto.ArticlesBackDTO;
import com.iksling.blog.dto.LabelBackDTO;
import com.iksling.blog.entity.*;
import com.iksling.blog.exception.FileStatusException;
import com.iksling.blog.exception.IllegalRequestException;
import com.iksling.blog.exception.OperationStatusException;
import com.iksling.blog.mapper.*;
import com.iksling.blog.pojo.LoginUser;
import com.iksling.blog.pojo.PagePojo;
import com.iksling.blog.service.ArticleService;
import com.iksling.blog.service.ArticleTagService;
import com.iksling.blog.util.*;
import com.iksling.blog.vo.ArticleBackVO;
import com.iksling.blog.vo.ArticleImageBackVO;
import com.iksling.blog.vo.ConditionBackVO;
import com.iksling.blog.vo.StatusBackVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

import static com.iksling.blog.constant.CommonConst.STATIC_RESOURCE_URL;
import static com.iksling.blog.constant.FlagConst.*;
import static com.iksling.blog.constant.RedisConst.ARTICLE_LIKE_COUNT;
import static com.iksling.blog.constant.RedisConst.ARTICLE_VIEW_COUNT;
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
        List<Map<String, Object>> mapList = multiFileMapper.selectMaps(new LambdaQueryWrapper<MultiFile>()
                .select(MultiFile::getId, MultiFile::getFileFullPath)
                .eq(MultiFile::getFileName, articleId)
                .exists("select id from tb_article where id="+articleId+" and userId="+articleUserId+" and deleted_flag=false and recycle_flag=false"));
        if (mapList.isEmpty())
            throw new OperationStatusException();
        long fileName = IdWorker.getId();
        String[] originalFilenameArr = Objects.requireNonNull(file.getOriginalFilename()).split("\\.");
        String targetAddr = mapList.get(0).get("file_full_path").toString();
        String fullFileName = fileName + "." + originalFilenameArr[1];
        if (MultiFileUtil.upload(file, targetAddr, fullFileName) == null)
            throw new FileStatusException("文件上传失败!");
        String iPAddress = IpUtil.getIpAddress(request);
        multiFileMapper.insert(MultiFile.builder()
                .userId(articleUserId)
                .parentId((Integer) mapList.get(0).get("id"))
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
        LambdaUpdateWrapper<Article> lambdaUpdateWrapper = new LambdaUpdateWrapper<Article>()
                .eq(loginUser.getRoleWeight() > 100, Article::getDeletedFlag, false)
                .eq(loginUser.getRoleWeight() > 300, Article::getUserId, loginUser.getUserId())
                .in(Article::getId, statusBackVO.getIdList())
                .set(Article::getUpdateUser, loginUser.getUserId())
                .set(Article::getUpdateTime, updateTime);
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
        List<Map<String, Object>> mapList = multiFileMapper.selectMaps(new LambdaQueryWrapper<MultiFile>()
                .select(MultiFile::getId, MultiFile::getFileFullPath, MultiFile::getFileExtension)
                .in(MultiFile::getFileName, fileNameList)
                .eq(MultiFile::getFileMark, IMAGE_ARTICLE.getCurrentPath())
                .eq(MultiFile::getDeletedCount, 0)
                .eq(loginUser.getRoleWeight() > 300, MultiFile::getUserId, loginUser.getUserId()));
        if (mapList.size() != fileNameList.size())
            throw new OperationStatusException();
        mapList.forEach(e -> {
            long fileNameNew = IdWorker.getId();
            String fileFullPath = e.get("file_full_path").toString();
            String fileFullPathOld = getSplitStringByIndex(fileFullPath, "[_.]", 0);
            String fileFullPathNew = fileFullPathOld + "_" + fileNameNew + "_" + DELETED + "." + e.get("file_extension");
            multiFileMapper.update(null, new LambdaUpdateWrapper<MultiFile>()
                    .set(MultiFile::getFileNameNew, fileNameNew)
                    .set(MultiFile::getFileFullPath, fileFullPathNew)
                    .set(MultiFile::getDeletedCount, 1)
                    .set(MultiFile::getUpdateUser, loginUser.getUserId())
                    .set(MultiFile::getUpdateTime, new Date())
                    .eq(MultiFile::getId, e.get("id")));
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
        List<Map<String, Object>> mapList = multiFileMapper.selectMaps(new LambdaQueryWrapper<MultiFile>()
                .select(MultiFile::getId, MultiFile::getFileFullPath)
                .in(MultiFile::getFileName, idList));
        if (deletedFlag)
            mapList.forEach(e -> {
                long fileNameNew = IdWorker.getId();
                String fileFullPath = e.get("file_full_path").toString();
                String fileFullPathOld = getSplitStringByIndex(fileFullPath, "_", 0);
                String fileFullPathNew = fileFullPathOld + "_" + fileNameNew + "_" + DELETED;
                multiFileMapper.update(null, new LambdaUpdateWrapper<MultiFile>()
                        .set(MultiFile::getFileNameNew, fileNameNew)
                        .set(MultiFile::getFileFullPath, fileFullPathNew)
                        .set(MultiFile::getDeletedCount, 1)
                        .set(MultiFile::getUpdateUser, loginUserId)
                        .set(MultiFile::getUpdateTime, updateTime)
                        .eq(MultiFile::getId, e.get("id")));
                multiFileMapper.update(null, new LambdaUpdateWrapper<MultiFile>()
                        .setSql("deleted_count=if(deleted_count>0,deleted_count+1,deleted_count-1),file_full_path=replace(file_full_path,'"+fileFullPath+"','"+fileFullPathNew+"')")
                        .eq(MultiFile::getParentId, e.get("id")));
                MultiFileUtil.rename(fileFullPath, fileFullPathNew);
            });
        else
            mapList.forEach(e -> {
                String fileFullPath = e.get("file_full_path").toString();
                String fileFullName = getSplitStringByIndex(fileFullPath, "/", -1);
                String fileFullPathOld = fileFullPath.substring(0, fileFullPath.length() - fileFullName.length()) + getSplitStringByIndex(fileFullName, "[_.]", 0);
                multiFileMapper.update(null, new LambdaUpdateWrapper<MultiFile>()
                        .set(MultiFile::getFileFullPath, fileFullPathOld)
                        .set(MultiFile::getDeletedCount, 0)
                        .set(MultiFile::getPublicFlag, true)
                        .set(MultiFile::getHiddenFlag, false)
                        .set(MultiFile::getUpdateUser, loginUserId)
                        .set(MultiFile::getUpdateTime, updateTime)
                        .eq(MultiFile::getId, e.get("id")));
                multiFileMapper.update(null, new LambdaUpdateWrapper<MultiFile>()
                        .setSql("deleted_count=if(deleted_count>0,deleted_count-1,deleted_count+1),file_full_path=replace(file_full_path,'"+fileFullPath+"','"+fileFullPathOld+"')")
                        .eq(MultiFile::getParentId, e.get("id")));
                MultiFileUtil.rename(fileFullPath, fileFullPathOld);
            });
    }

    private void deleteArticleDirByIdList(List<Integer> idList) {
        List<Map<String, Object>> mapList = multiFileMapper.selectMaps(new LambdaQueryWrapper<MultiFile>()
                .select(MultiFile::getId, MultiFile::getFileFullPath)
                .in(MultiFile::getFileName, idList));
        mapList.forEach(e -> {
            multiFileMapper.delete(new LambdaUpdateWrapper<MultiFile>()
                    .eq(MultiFile::getId, e.get("id"))
                    .or()
                    .eq(MultiFile::getParentId, e.get("id")));
            MultiFileUtil.delete(e.get("file_full_path").toString());
        });
    }
}




