package com.iksling.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iksling.blog.dto.MultiFilesBackDTO;
import com.iksling.blog.entity.Article;
import com.iksling.blog.entity.MultiFile;
import com.iksling.blog.entity.User;
import com.iksling.blog.entity.UserAuth;
import com.iksling.blog.exception.FileStatusException;
import com.iksling.blog.exception.OperationStatusException;
import com.iksling.blog.mapper.ArticleMapper;
import com.iksling.blog.mapper.MultiFileMapper;
import com.iksling.blog.mapper.UserAuthMapper;
import com.iksling.blog.mapper.UserMapper;
import com.iksling.blog.pojo.LoginUser;
import com.iksling.blog.service.MultiFileService;
import com.iksling.blog.util.*;
import com.iksling.blog.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.iksling.blog.constant.CommonConst.STATIC_RESOURCE_URL;
import static com.iksling.blog.constant.FlagConst.*;
import static com.iksling.blog.enums.FileEnum.IMG_ARTICLE;
import static com.iksling.blog.enums.FileEnum.IMG_AVATAR;
import static com.iksling.blog.util.CommonUtil.getSplitStringByIndex;

/**
 *
 */
@Service
public class MultiFileServiceImpl extends ServiceImpl<MultiFileMapper, MultiFile>
    implements MultiFileService{
    @Autowired
    private MultiFileMapper multiFileMapper;

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserAuthMapper userAuthMapper;
    @Autowired
    private ArticleMapper articleMapper;

    @Resource
    private HttpServletRequest request;

    @Override
    @Transactional
    public void saveOrUpdateMultiFileBackVO(MultiFileBackVO multiFileBackVO) {
        LoginUser loginUser = UserUtil.getLoginUser();
        Integer loginUserId = loginUser.getUserId();
        MultiFile multiFile = BeanCopyUtil.copyObject(multiFileBackVO, MultiFile.class);
        if (multiFile.getId() == null) {
            if (multiFile.getFileNameOrigin() == null)
                throw new OperationStatusException();
            long fileName = IdWorker.getId();
            if (multiFile.getParentId() == null) {
                multiFile.setFileFullPath(loginUserId + "/" + fileName);
            } else {
                List<Object> objectList = multiFileMapper.selectObjs(new LambdaQueryWrapper<MultiFile>()
                        .select(MultiFile::getFileFullPath)
                        .eq(MultiFile::getId, multiFile.getParentId())
                        .eq(MultiFile::getUserId, loginUserId)
                        .eq(MultiFile::getFileMark, 0)
                        .eq(MultiFile::getDeletedFlag, false)
                        .eq(MultiFile::getDeletableFlag, true));
                if (objectList.isEmpty())
                    throw new OperationStatusException();
                multiFile.setFileFullPath(objectList.get(0) + "/" + fileName);
            }
            if (CommonUtil.isNotEmpty(multiFile.getFileCover()) && !multiFile.getFileCover().startsWith(STATIC_RESOURCE_URL))
                multiFile.setFileCover(null);
            String iPAddress = IpUtil.getIpAddress(request);
            multiFile.setUserId(loginUserId);
            multiFile.setFileName(fileName);
            multiFile.setIpSource(IpUtil.getIpSource(iPAddress));
            multiFile.setIpAddress(iPAddress);
            multiFile.setCreateUser(loginUserId);
            multiFile.setCreateTime(new Date());
            multiFileMapper.insert(multiFile);
        } else {
            Integer count = multiFileMapper.selectCount(new LambdaQueryWrapper<MultiFile>()
                    .eq(MultiFile::getId, multiFile.getId())
                    .eq(loginUser.getRoleWeight() > 200, MultiFile::getUserId, loginUserId)
                    .eq(MultiFile::getDeletedFlag, false)
                    .eq(MultiFile::getDeletableFlag, true));
            if (count != 1)
                throw new OperationStatusException();
            if (CommonUtil.isNotEmpty(multiFile.getFileCover()) && !multiFile.getFileCover().startsWith(STATIC_RESOURCE_URL))
                multiFile.setFileCover("");
            multiFile.setUpdateUser(loginUserId);
            multiFile.setUpdateTime(new Date());
            multiFileMapper.updateById(multiFile);
        }
    }

    @Override
    @Transactional
    public String saveArticleImageBackVO(ArticleImageBackVO articleImageBackVO) {
        MultipartFile file = articleImageBackVO.getFile();
        if (file.isEmpty())
            throw new FileStatusException("文件不存在!");
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null)
            throw new FileStatusException("文件解析异常!");
        String extension = getSplitStringByIndex(originalFilename, "\\.", -1);
        if (MultiFileUtil.checkNotValidFileType(extension, IMG_ARTICLE.getType()))
            throw new FileStatusException("文件类型不匹配!需要的文件类型为{.jpg .jpeg .png .gif}");
        if (MultiFileUtil.checkNotValidFileSize(file.getSize(), IMG_ARTICLE.getSize(), IMG_ARTICLE.getUnit()))
            throw new FileStatusException("文件大小超出限制!文件最大为{" + IMG_ARTICLE.getSize() + IMG_ARTICLE.getUnit() + "}");
        LoginUser loginUser = UserUtil.getLoginUser();
        Integer articleUserId = articleImageBackVO.getUserId();
        if (articleUserId == null)
            articleUserId = loginUser.getUserId();
        else if (loginUser.getRoleWeight() > 300 && !loginUser.getUserId().equals(articleUserId))
            throw new OperationStatusException();
        Integer articleId = articleImageBackVO.getArticleId();
        Integer count = articleMapper.selectCount(new LambdaQueryWrapper<Article>()
                .eq(Article::getId, articleId)
                .eq(Article::getUserId, articleUserId)
                .eq(Article::getDeletedFlag, false)
                .eq(Article::getRecycleFlag, false));
        if (count != 1)
            throw new OperationStatusException();
        long fileName = IdWorker.getId();
        String targetAddr = articleUserId + "/" + IMG_ARTICLE.getPath() + "/" + articleId;
        String fullFileName = fileName + "." + extension;
        String url = MultiFileUtil.upload(file, targetAddr, fullFileName);
        if (url == null)
            throw new FileStatusException("文件上传失败!");
        articleImageBackVO.setFile(null);
        String iPAddress = IpUtil.getIpAddress(request);
        List<Object> objectList = multiFileMapper.selectObjs(new LambdaQueryWrapper<MultiFile>()
                .select(MultiFile::getId)
                .eq(MultiFile::getFileName, articleId));
        multiFileMapper.insert(MultiFile.builder()
                .userId(articleUserId)
                .parentId((Integer) objectList.get(0))
                .fileDesc("{'userId':"+articleUserId+",'articleId':"+articleId+"}")
                .fileMark(IMG_ARTICLE.getCurrentPath().intValue())
                .fileName(fileName)
                .fileSize(file.getSize())
                .fileFullPath(targetAddr + "/" + fullFileName)
                .fileExtension(extension)
                .fileNameOrigin(originalFilename)
                .ipSource(IpUtil.getIpSource(iPAddress))
                .ipAddress(iPAddress)
                .createUser(loginUser.getUserId())
                .createTime(new Date())
                .build());
        return url;
    }

    @Override
    @Transactional
    public String saveUserAvatarBackVO(UserAvatarBackVO userAvatarBackVO) {
        MultipartFile file = userAvatarBackVO.getFile();
        if (file.isEmpty())
            throw new FileStatusException("文件不存在!");
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null)
            throw new FileStatusException("文件解析异常!");
        String extension = getSplitStringByIndex(originalFilename, "\\.", -1);
        if (MultiFileUtil.checkNotValidFileType(extension, IMG_AVATAR.getType()))
            throw new FileStatusException("文件类型不匹配!需要的文件类型为{.jpg .jpeg .png .gif}");
        if (MultiFileUtil.checkNotValidFileSize(file.getSize(), IMG_AVATAR.getSize(), IMG_AVATAR.getUnit()))
            throw new FileStatusException("文件大小超出限制!文件最大为{" + IMG_AVATAR.getSize() + IMG_AVATAR.getUnit() + "}");
        Integer userId = userAvatarBackVO.getUserId();
        LoginUser loginUser = UserUtil.getLoginUser();
        if (userId == null)
            userId = loginUser.getUserId();
        else if (loginUser.getRoleWeight() > 200 && !loginUser.getUserId().equals(userId))
            throw new OperationStatusException();
        Integer count = userAuthMapper.selectCount(new LambdaQueryWrapper<UserAuth>()
                .eq(UserAuth::getUserId, userId)
                .eq(UserAuth::getDeletedFlag, false));
        if (count != 1)
            throw new OperationStatusException();
        long fileName = IdWorker.getId();
        String targetAddr = userId + "/" + IMG_AVATAR.getPath();
        String fullFileName = fileName + "." + extension;
        String url = MultiFileUtil.upload(file, targetAddr, fullFileName);
        if (url == null)
            throw new FileStatusException("文件上传失败!");
        userAvatarBackVO.setFile(null);
        List<Object> objectList = multiFileMapper.selectObjs(new LambdaQueryWrapper<MultiFile>()
                .select(MultiFile::getId)
                .eq(MultiFile::getUserId, userId)
                .eq(MultiFile::getFileName, IMG_AVATAR.getCurrentPath()));
        String iPAddress = IpUtil.getIpAddress(request);
        multiFileMapper.insert(MultiFile.builder()
                .userId(userId)
                .parentId((Integer) objectList.get(0))
                .fileDesc("{'userId':"+userId+"}")
                .fileMark(IMG_AVATAR.getCurrentPath().intValue())
                .fileName(fileName)
                .fileSize(file.getSize())
                .fileFullPath(targetAddr + "/" + fullFileName)
                .fileExtension(extension)
                .fileNameOrigin(originalFilename)
                .ipSource(IpUtil.getIpSource(iPAddress))
                .ipAddress(iPAddress)
                .createUser(loginUser.getUserId())
                .createTime(new Date())
                .build());
        return url;
    }

    @Override
    @Transactional
    public void updateMultiFileStatusBackVO(StatusBackVO statusBackVO) {
        LoginUser loginUser = UserUtil.getLoginUser();
        Integer loginUserId = loginUser.getUserId();
        List<Map<String, Object>> mapList = multiFileMapper.selectMaps(new LambdaQueryWrapper<MultiFile>()
                .select(MultiFile::getId, MultiFile::getFileMark, MultiFile::getFileFullPath, MultiFile::getFileExtension, MultiFile::getPublicFlag, MultiFile::getHiddenFlag)
                .in(MultiFile::getId, statusBackVO.getIdList())
                .eq(loginUser.getRoleWeight() > 200, MultiFile::getUserId, loginUserId)
                .eq(MultiFile::getDeletedFlag, false)
                .eq(MultiFile::getDeletableFlag, true));
        if (mapList.size() != statusBackVO.getIdList().size())
            throw new OperationStatusException();
        if (HIDDEN.equals(statusBackVO.getType())) {
            mapList.forEach(e -> {
                long fileNameNew = IdWorker.getId();
                String fileFullPath = e.get("file_full_path").toString();
                String fileFullPathOld = getSplitStringByIndex(fileFullPath, "[_.]", 0);
                String fileFullPathNew = fileFullPathOld + "_" + fileNameNew + "_";
                if (e.get("hidden_flag").equals(false)) {
                    fileFullPathNew += fileFullPathNew  + HIDDEN;
                    if (e.get("file_mark").equals(0)) {
                        multiFileMapper.update(null, new LambdaUpdateWrapper<MultiFile>()
                                .set(MultiFile::getFileNameNew, fileNameNew)
                                .set(MultiFile::getHiddenFlag, true)
                                .eq(MultiFile::getId, e.get("id")));
                        multiFileMapper.update(null, new LambdaUpdateWrapper<MultiFile>()
                                .setSql("file_full_path=replace(file_full_path,"+fileFullPath+","+fileFullPathNew+")")
                                .likeRight(MultiFile::getFileFullPath, fileFullPath));
                    } else {
                        fileFullPathNew += "." + e.get("file_extension");
                        multiFileMapper.update(null, new LambdaUpdateWrapper<MultiFile>()
                                .set(MultiFile::getFileFullPath, fileFullPathNew)
                                .set(MultiFile::getFileNameNew, fileNameNew)
                                .set(MultiFile::getHiddenFlag, true)
                                .eq(MultiFile::getId, e.get("id")));
                    }
                } else if (e.get("public_flag").equals(false)) {
                    fileFullPathNew += fileFullPathNew + PUBLIC;
                    if (e.get("file_mark").equals(0)) {
                        multiFileMapper.update(null, new LambdaUpdateWrapper<MultiFile>()
                                .set(MultiFile::getFileNameNew, fileNameNew)
                                .set(MultiFile::getHiddenFlag, false)
                                .eq(MultiFile::getId, e.get("id")));
                        multiFileMapper.update(null, new LambdaUpdateWrapper<MultiFile>()
                                .setSql("file_full_path=replace(file_full_path,"+fileFullPath+","+fileFullPathNew+")")
                                .likeRight(MultiFile::getFileFullPath, fileFullPath));
                    } else {
                        fileFullPathNew += fileFullPathNew + "." + e.get("file_extension");
                        multiFileMapper.update(null, new LambdaUpdateWrapper<MultiFile>()
                                .set(MultiFile::getFileFullPath, fileFullPathNew)
                                .set(MultiFile::getFileNameNew, fileNameNew)
                                .set(MultiFile::getHiddenFlag, false)
                                .eq(MultiFile::getId, e.get("id")));
                    }
                } else {
                    if (e.get("file_mark").equals(0)) {
                        fileFullPathNew = fileFullPathOld;
                        multiFileMapper.update(null, new LambdaUpdateWrapper<MultiFile>()
                                .set(MultiFile::getHiddenFlag, false)
                                .eq(MultiFile::getId, e.get("id")));
                        multiFileMapper.update(null, new LambdaUpdateWrapper<MultiFile>()
                                .setSql("file_full_path=replace(file_full_path,"+fileFullPath+","+fileFullPathNew+")")
                                .likeRight(MultiFile::getFileFullPath, fileFullPath));
                    } else {
                        fileFullPathNew = fileFullPathOld + "." + e.get("file_extension");
                        multiFileMapper.update(null, new LambdaUpdateWrapper<MultiFile>()
                                .set(MultiFile::getFileFullPath, fileFullPathNew)
                                .set(MultiFile::getHiddenFlag, false)
                                .eq(MultiFile::getId, e.get("id")));
                    }
                }
                MultiFileUtil.rename(fileFullPath, fileFullPathNew);
            });
        } else {
            mapList.forEach(e -> {
                if (e.get("hidden_flag").equals(false)) {
                    long fileNameNew = IdWorker.getId();
                    String fileFullPath = e.get("file_full_path").toString();
                    String fileFullPathOld = getSplitStringByIndex(fileFullPath, "[_.]", 0);
                    String fileFullPathNew = fileFullPathOld + "_" + fileNameNew + "_" + PUBLIC;
                    if (e.get("public_flag").equals(true)) {
                        if (e.get("file_mark").equals(0)) {
                            multiFileMapper.update(null, new LambdaUpdateWrapper<MultiFile>()
                                    .set(MultiFile::getFileNameNew, fileNameNew)
                                    .set(MultiFile::getPublicFlag, false)
                                    .eq(MultiFile::getId, e.get("id")));
                            multiFileMapper.update(null, new LambdaUpdateWrapper<MultiFile>()
                                    .setSql("file_full_path=replace(file_full_path,"+fileFullPath+","+fileFullPathNew+")")
                                    .likeRight(MultiFile::getFileFullPath, fileFullPath));
                        } else {
                            fileFullPathNew += fileFullPathNew + "." + e.get("file_extension");
                            multiFileMapper.update(null, new LambdaUpdateWrapper<MultiFile>()
                                    .set(MultiFile::getFileFullPath, fileFullPathNew)
                                    .set(MultiFile::getFileNameNew, fileNameNew)
                                    .set(MultiFile::getPublicFlag, false)
                                    .eq(MultiFile::getId, e.get("id")));
                        }
                    } else {
                        if (e.get("file_mark").equals(0)) {
                            fileFullPathNew = fileFullPathOld;
                            multiFileMapper.update(null, new LambdaUpdateWrapper<MultiFile>()
                                    .set(MultiFile::getPublicFlag, true)
                                    .eq(MultiFile::getId, e.get("id")));
                            multiFileMapper.update(null, new LambdaUpdateWrapper<MultiFile>()
                                    .setSql("file_full_path=replace(file_full_path,"+fileFullPath+","+fileFullPathNew+")")
                                    .likeRight(MultiFile::getFileFullPath, fileFullPath));
                        } else {
                            fileFullPathNew = fileFullPathOld + "." + e.get("file_extension");
                            multiFileMapper.update(null, new LambdaUpdateWrapper<MultiFile>()
                                    .set(MultiFile::getFileFullPath, fileFullPathNew)
                                    .set(MultiFile::getPublicFlag, true)
                                    .eq(MultiFile::getId, e.get("id")));
                        }
                    }
                    MultiFileUtil.rename(fileFullPath, fileFullPathNew);
                } else {
                    multiFileMapper.update(null, new LambdaUpdateWrapper<MultiFile>()
                            .setSql("public_flag=!public_flag")
                            .eq(MultiFile::getId, e.get("id")));
                }
            });
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
                .eq(MultiFile::getFileMark, IMG_ARTICLE.getCurrentPath())
                .eq(MultiFile::getDeletedFlag, false)
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
                    .set(MultiFile::getDeletedFlag, true)
                    .set(MultiFile::getUpdateUser, loginUser.getUserId())
                    .set(MultiFile::getUpdateTime, new Date())
                    .eq(MultiFile::getId, e.get("id")));
            MultiFileUtil.rename(fileFullPath, fileFullPathNew);
        });
    }

    @Override
    @Transactional
    public void updateBackUserAvatarsByFileNameList(List<Long> fileNameList) {
        LoginUser loginUser = UserUtil.getLoginUser();
        List<Map<String, Object>> mapList = multiFileMapper.selectMaps(new LambdaQueryWrapper<MultiFile>()
                .select(MultiFile::getId, MultiFile::getFileFullPath, MultiFile::getFileExtension)
                .in(MultiFile::getFileName, fileNameList)
                .eq(MultiFile::getFileMark, IMG_AVATAR.getCurrentPath())
                .eq(MultiFile::getDeletedFlag, false)
                .eq(loginUser.getRoleWeight() > 200, MultiFile::getUserId, loginUser.getUserId()));
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
                    .set(MultiFile::getDeletedFlag, true)
                    .set(MultiFile::getUpdateUser, loginUser.getUserId())
                    .set(MultiFile::getUpdateTime, new Date())
                    .eq(MultiFile::getId, e.get("id")));
            MultiFileUtil.rename(fileFullPath, fileFullPathNew);
        });
    }

    @Override
    @Transactional
    public String updateUserAvatarVO(UserAvatarVO userAvatarVO) {
        MultipartFile file = userAvatarVO.getFile();
        if (file.isEmpty())
            throw new FileStatusException("文件不存在!");
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null)
            throw new FileStatusException("文件解析异常!");
        String extension = getSplitStringByIndex(originalFilename, "\\.", -1);
        if (MultiFileUtil.checkNotValidFileType(extension, IMG_AVATAR.getType()))
            throw new FileStatusException("文件类型不匹配!需要的文件类型为{.jpg .jpeg .png .gif}");
        if (MultiFileUtil.checkNotValidFileSize(file.getSize(), IMG_AVATAR.getSize(), IMG_AVATAR.getUnit()))
            throw new FileStatusException("文件大小超出限制!文件最大为{" + IMG_AVATAR.getSize() + IMG_AVATAR.getUnit() + "}");
        long fileName = IdWorker.getId();
        Integer loginUserId = UserUtil.getLoginUser().getUserId();
        String targetAddr = loginUserId + "/" + IMG_AVATAR.getPath();
        String fullFileName = fileName + "." + extension;
        String url = MultiFileUtil.upload(file, targetAddr, fullFileName);
        if (url == null)
            throw new FileStatusException("文件上传失败!");
        userAvatarVO.setFile(null);
        Date dateTime = new Date();
        List<Object> objectList = multiFileMapper.selectObjs(new LambdaQueryWrapper<MultiFile>()
                .select(MultiFile::getFileFullPath)
                .eq(MultiFile::getUserId, loginUserId)
                .eq(MultiFile::getFileMark, IMG_AVATAR.getCurrentPath())
                .eq(MultiFile::getDeletedFlag, false));
        if (!objectList.isEmpty())
            updateUserAvatarBy(loginUserId, objectList.get(0).toString(), dateTime);
        userMapper.update(null, new LambdaUpdateWrapper<User>()
                .set(User::getAvatar, url)
                .set(User::getUpdateUser, loginUserId)
                .set(User::getUpdateTime, dateTime)
                .eq(User::getId, loginUserId));
        objectList = multiFileMapper.selectObjs(new LambdaQueryWrapper<MultiFile>()
                .select(MultiFile::getId)
                .eq(MultiFile::getUserId, loginUserId)
                .eq(MultiFile::getFileName, IMG_AVATAR.getCurrentPath()));
        String iPAddress = IpUtil.getIpAddress(request);
        multiFileMapper.insert(MultiFile.builder()
                .userId(loginUserId)
                .parentId((Integer) objectList.get(0))
                .fileDesc("{'userId':"+loginUserId+"}")
                .fileMark(IMG_AVATAR.getCurrentPath().intValue())
                .fileName(fileName)
                .fileSize(file.getSize())
                .fileFullPath(targetAddr + "/" + fullFileName)
                .fileExtension(extension)
                .fileNameOrigin(originalFilename)
                .ipSource(IpUtil.getIpSource(iPAddress))
                .ipAddress(iPAddress)
                .createUser(loginUserId)
                .createTime(dateTime)
                .build());
        return url;
    }

    @Override
    public void updateArticleImageBy(Integer loginUserId, Integer articleId, String fileFullPath, Date updateTime) {
        long fileNameNew = IdWorker.getId();
        String[] pathArr = fileFullPath.split("[_.]");
        String fileFullPathOld = pathArr[0];
        String fileFullPathNew = fileFullPathOld + "_" + fileNameNew + "_" + DELETED + "." + pathArr[pathArr.length - 1];
        multiFileMapper.update(null, new LambdaUpdateWrapper<MultiFile>()
                .set(MultiFile::getFileNameNew, fileNameNew)
                .set(MultiFile::getFileFullPath, fileFullPathNew)
                .set(MultiFile::getDeletedFlag, true)
                .set(MultiFile::getUpdateUser, loginUserId)
                .set(MultiFile::getUpdateTime, updateTime)
                .eq(MultiFile::getFileName, getSplitStringByIndex(fileFullPathOld, "/", -1))
                .inSql(MultiFile::getParentId, "select id from tb_multi_file where file_name="+articleId));
        MultiFileUtil.rename(fileFullPathOld, fileFullPathNew);
    }

    @Override
    public void updateUserAvatarBy(Integer loginUserId, String fileFullPath, Date updateTime) {
        long fileNameNew = IdWorker.getId();
        String[] pathArr = fileFullPath.split("\\.");
        String fileFullPathOld = pathArr[0];
        String fileFullPathNew = fileFullPathOld + "_" + fileNameNew + "_" + HIDDEN + "." + pathArr[pathArr.length - 1];
        multiFileMapper.update(null, new LambdaUpdateWrapper<MultiFile>()
                .set(MultiFile::getFileNameNew, fileNameNew)
                .set(MultiFile::getFileFullPath, fileFullPathNew)
                .set(MultiFile::getDeletedFlag, true)
                .set(MultiFile::getHiddenFlag, true)
                .set(MultiFile::getUpdateUser, loginUserId)
                .set(MultiFile::getUpdateTime, updateTime)
                .eq(MultiFile::getFileName, getSplitStringByIndex(fileFullPathOld, "/", -1)));
        MultiFileUtil.rename(fileFullPathOld, fileFullPathNew);
    }

    @Override
    @Transactional
    public void saveArticleDirById(Integer id, Integer loginUserId, Date createTime) {
        List<Object> objectList = multiFileMapper.selectObjs(new LambdaQueryWrapper<MultiFile>()
                .select(MultiFile::getId)
                .eq(MultiFile::getUserId, loginUserId)
                .eq(MultiFile::getFileName, IMG_ARTICLE.getCurrentPath()));
        multiFileMapper.insert(MultiFile.builder()
                .userId(loginUserId)
                .parentId((Integer) objectList.get(0))
                .fileDesc("{'articleId':"+id+"}")
                .fileName(Long.valueOf(id))
                .fileFullPath(loginUserId + "/" + IMG_ARTICLE.getPath() + "/" + id)
                .fileNameOrigin(id.toString())
                .deletableFlag(true)
                .createUser(loginUserId)
                .createTime(createTime)
                .build());
    }

    @Override
    @Transactional
    public void updateArticleDirByIdList(List<Integer> idList, Integer loginUserId, Date updateTime) {
        List<Map<String, Object>> mapList = multiFileMapper.selectMaps(new LambdaQueryWrapper<MultiFile>()
                .select(MultiFile::getId, MultiFile::getFileFullPath)
                .in(MultiFile::getFileName, idList));
        mapList.forEach(e -> {
            long fileNameNew = IdWorker.getId();
            String fileFullPath = e.get("file_full_path").toString();
            String fileFullPathOld = getSplitStringByIndex(fileFullPath, "_", 0);
            String fileFullPathNew = fileFullPathOld + "_" + fileNameNew + "_" + DELETED;
            multiFileMapper.update(null, new LambdaUpdateWrapper<MultiFile>()
                    .set(MultiFile::getFileNameNew, fileNameNew)
                    .set(MultiFile::getFileFullPath, fileFullPathNew)
                    .set(MultiFile::getDeletedFlag, true)
                    .set(MultiFile::getUpdateUser, loginUserId)
                    .set(MultiFile::getUpdateTime, updateTime)
                    .eq(MultiFile::getId, e.get("id")));
            MultiFileUtil.rename(fileFullPath, fileFullPathNew);
        });
    }

    @Override
    @Transactional
    public void deleteArticleDirByIdList(List<Integer> idList) {
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

    @Override
    public List<MultiFilesBackDTO> getMultiFilesBackDTO(ConditionBackVO condition) {
        LoginUser loginUser = UserUtil.getLoginUser();
        LambdaQueryWrapper<MultiFile> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (DELETED.equals(condition.getType())) {
            if (loginUser.getRoleWeight() > 100)
                return new ArrayList<>();
            lambdaQueryWrapper.eq(MultiFile::getDeletedFlag, true);
        } else
            lambdaQueryWrapper.eq(MultiFile::getDeletedFlag, false);
        if (condition.getUserId() == null || loginUser.getRoleWeight() > 200)
            lambdaQueryWrapper.eq(MultiFile::getUserId, loginUser.getUserId());
        else
            lambdaQueryWrapper.eq(MultiFile::getUserId, condition.getUserId());
        if (CommonUtil.isEmpty(condition.getKeywords()))
            lambdaQueryWrapper.eq(MultiFile::getParentId, -1);
        else
            lambdaQueryWrapper.like(MultiFile::getFileNameOrigin, condition.getKeywords());
        List<MultiFile> multiFileList = multiFileMapper.selectList(lambdaQueryWrapper
                .select(MultiFile::getId, MultiFile::getUserId, MultiFile::getFileDesc,
                        MultiFile::getFileSize, MultiFile::getFileCover,
                        MultiFile::getFileFullPath, MultiFile::getFileExtension, MultiFile::getFileNameOrigin,
                        MultiFile::getPublicFlag, MultiFile::getHiddenFlag,MultiFile::getCreateTime,
                        MultiFile::getUpdateTime));
        return  multiFileList.stream()
                .map(e -> {
                    MultiFilesBackDTO multiFilesBackDTO = BeanCopyUtil.copyObject(e, MultiFilesBackDTO.class);
                    multiFilesBackDTO.setHasChildren(multiFilesBackDTO.getFileExtension().length() == 0);
                    return multiFilesBackDTO;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<MultiFilesBackDTO> getMultiFilesBackDTOById(Integer id) {
        LoginUser loginUser = UserUtil.getLoginUser();
        List<MultiFile> multiFileList = multiFileMapper.selectList(new LambdaQueryWrapper<MultiFile>()
                .select(MultiFile::getId, MultiFile::getUserId, MultiFile::getFileDesc,
                        MultiFile::getFileSize, MultiFile::getFileCover,
                        MultiFile::getFileFullPath, MultiFile::getFileExtension, MultiFile::getFileNameOrigin,
                        MultiFile::getPublicFlag, MultiFile::getHiddenFlag, MultiFile::getCreateTime,
                        MultiFile::getUpdateTime)
                .eq(loginUser.getRoleWeight() > 100, MultiFile::getDeletedFlag, false)
                .eq(loginUser.getRoleWeight() > 200, MultiFile::getUserId, loginUser.getUserId())
                .eq(MultiFile::getParentId, id));
        return  multiFileList.stream()
                .map(e -> {
                    MultiFilesBackDTO multiFilesBackDTO = BeanCopyUtil.copyObject(e, MultiFilesBackDTO.class);
                    multiFilesBackDTO.setHasChildren(multiFilesBackDTO.getFileExtension().length() == 0);
                    return multiFilesBackDTO;
                })
                .collect(Collectors.toList());
    }
}




