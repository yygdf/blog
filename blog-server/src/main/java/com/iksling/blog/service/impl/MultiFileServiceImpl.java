package com.iksling.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iksling.blog.entity.*;
import com.iksling.blog.exception.FileStatusException;
import com.iksling.blog.exception.OperationStatusException;
import com.iksling.blog.mapper.ArticleMapper;
import com.iksling.blog.mapper.MultiFileMapper;
import com.iksling.blog.mapper.UserAuthMapper;
import com.iksling.blog.mapper.UserMapper;
import com.iksling.blog.pojo.LoginUser;
import com.iksling.blog.service.MultiFileService;
import com.iksling.blog.util.IpUtil;
import com.iksling.blog.util.MultiFileUtil;
import com.iksling.blog.util.UserUtil;
import com.iksling.blog.vo.ArticleImageBackVO;
import com.iksling.blog.vo.UserAvatarBackVO;
import com.iksling.blog.vo.UserAvatarVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
                .fileFullPath(targetAddr + "/" + fullFileName)
                .fileExtension(extension)
                .fileNameOrigin(originalFilename)
                .ipAddress(iPAddress)
                .ipSource(IpUtil.getIpSource(iPAddress))
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
                .fileFullPath(targetAddr + "/" + fullFileName)
                .fileExtension(extension)
                .fileNameOrigin(originalFilename)
                .ipAddress(iPAddress)
                .ipSource(IpUtil.getIpSource(iPAddress))
                .createUser(loginUser.getUserId())
                .createTime(new Date())
                .build());
        return url;
    }

    @Override
    @Transactional
    public void updateBackArticleImagesByFileNameList(List<Long> fileNameList) {
        if (fileNameList.isEmpty())
            throw new OperationStatusException();
        LoginUser loginUser = UserUtil.getLoginUser();
        List<Map<String, Object>> mapList = multiFileMapper.selectArticleImgFileByFileName(fileNameList, IMG_ARTICLE.getCurrentPath().intValue(), loginUser.getRoleWeight(), loginUser.getUserId());
        if (mapList.size() != fileNameList.size())
            throw new OperationStatusException();
        mapList.forEach(e -> {
            long fileNameNew = IdWorker.getId();
            multiFileMapper.update(null, new LambdaUpdateWrapper<MultiFile>()
                    .set(MultiFile::getFileNameNew, fileNameNew)
                    .set(MultiFile::getDeletedFlag, true)
                    .set(MultiFile::getUpdateUser, loginUser.getUserId())
                    .set(MultiFile::getUpdateTime, new Date())
                    .eq(MultiFile::getId, e.get("id")));
            String frontPath = e.get("user_id") + "/" + IMG_ARTICLE.getPath() + "/" + e.get("file_name_parent") + "/" + e.get("file_name");
            String fullExtension = "." + e.get("file_extension");
            MultiFileUtil.rename(frontPath + fullExtension, frontPath + "-" + fileNameNew + "-article-DEL" + fullExtension);
        });
    }

    @Override
    @Transactional
    public void updateBackUserAvatarsByFileNameList(List<Long> fileNameList) {
        LoginUser loginUser = UserUtil.getLoginUser();
        List<Map<String, Object>> mapList = multiFileMapper.selectMaps(new QueryWrapper<MultiFile>()
                .select("id", "user_id", "file_name", "file_extension")
                .in("file_name", fileNameList)
                .eq("file_mark", IMG_AVATAR.getCurrentPath())
                .eq(loginUser.getRoleWeight() > 200, "user_id", loginUser.getUserId())
                .eq("deleted_flag", false));
        if (mapList.size() != fileNameList.size())
            throw new OperationStatusException();
        mapList.forEach(e -> {
            long fileNameNew = IdWorker.getId();
            multiFileMapper.update(null, new LambdaUpdateWrapper<MultiFile>()
                    .set(MultiFile::getFileNameNew, fileNameNew)
                    .set(MultiFile::getDeletedFlag, true)
                    .set(MultiFile::getUpdateUser, loginUser.getUserId())
                    .set(MultiFile::getUpdateTime, new Date())
                    .eq(MultiFile::getId, e.get("id")));
            String frontPath = e.get("user_id") + "/" + IMG_AVATAR.getPath() + "/" + e.get("file_name");
            String fullExtension = "." + e.get("file_extension");
            MultiFileUtil.rename(frontPath + fullExtension, frontPath + "-" + fileNameNew + "-avatar-DEL" + fullExtension);
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
        List<Map<String, Object>> mapList = multiFileMapper.selectMaps(new LambdaQueryWrapper<MultiFile>()
                .select(MultiFile::getFileName, MultiFile::getFileExtension)
                .eq(MultiFile::getUserId, loginUserId)
                .eq(MultiFile::getFileMark, IMG_AVATAR.getCurrentPath())
                .eq(MultiFile::getDeletedFlag, false));
        if (!mapList.isEmpty())
            updateUserAvatarBy(loginUserId, loginUserId, (Long) mapList.get(0).get("file_name"), mapList.get(0).get("file_extension").toString(), dateTime);
        userMapper.update(null, new LambdaUpdateWrapper<User>()
                .set(User::getAvatar, url)
                .set(User::getUpdateUser, loginUserId)
                .set(User::getUpdateTime, dateTime)
                .eq(User::getId, loginUserId));
        List<Object> objectList = multiFileMapper.selectObjs(new LambdaQueryWrapper<MultiFile>()
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
                .fileFullPath(targetAddr + "/" + fullFileName)
                .fileExtension(extension)
                .fileNameOrigin(originalFilename)
                .ipAddress(iPAddress)
                .ipSource(IpUtil.getIpSource(iPAddress))
                .createUser(loginUserId)
                .createTime(dateTime)
                .build());
        return url;
    }

    @Override
    public void updateArticleImageBy(Integer userId, Integer loginUserId, Integer articleId, String fullFileName, Date updateTime) {
        String[] fileNameArr = fullFileName.split("\\.");
        long fileNameNew = IdWorker.getId();
        long fileNameOld = Long.parseLong(fileNameArr[0]);
        multiFileMapper.update(null, new LambdaUpdateWrapper<MultiFile>()
                .set(MultiFile::getFileNameNew, fileNameNew)
                .set(MultiFile::getDeletedFlag, true)
                .set(MultiFile::getUpdateUser, loginUserId)
                .set(MultiFile::getUpdateTime, updateTime)
                .eq(MultiFile::getFileName, fileNameOld));
        String frontPath = userId + "/" + IMG_ARTICLE.getPath() + "/" + articleId + "/" + fileNameOld;
        String fullExtension = "." + fileNameArr[1];
        MultiFileUtil.rename(frontPath + fullExtension, frontPath + "-" + fileNameNew + "-article-DEL" + fullExtension);
    }

    @Override
    public void updateUserAvatarBy(Integer userId, Integer loginUserId, String fullFileName, Date updateTime) {
        String[] fileNameArr = fullFileName.split("\\.");
        updateUserAvatarBy(userId, loginUserId, Long.parseLong(fileNameArr[0]), fileNameArr[1], updateTime);
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
                .fileFullPath(loginUserId + "/" + IMG_ARTICLE.getPath() + "/" + id)
                .fileNameOrigin(id.toString())
                .deletableFlag(false)
                .createUser(loginUserId)
                .createTime(createTime)
                .build());
    }

    @Override
    @Transactional
    public void updateArticleDirByIdList(List<Integer> idList, Integer loginUserId, Date updateTime) {
        idList.forEach(
                e -> {
                    List<Map<String, Object>> mapList = multiFileMapper.selectMaps(new LambdaQueryWrapper<MultiFile>()
                            .select(MultiFile::getId, MultiFile::getUserId)
                            .eq(MultiFile::getFileName, e));
                    long fileNameNew = IdWorker.getId();
                    multiFileMapper.update(null, new LambdaUpdateWrapper<MultiFile>()
                            .set(MultiFile::getFileNameNew, fileNameNew)
                            .set(MultiFile::getDeletedFlag, true)
                            .set(MultiFile::getUpdateUser, loginUserId)
                            .set(MultiFile::getUpdateTime, updateTime)
                            .eq(MultiFile::getId, mapList.get(0).get("id")));
                    String uri = mapList.get(0).get("user_id") + "/" + IMG_ARTICLE.getPath() + "/" + e;
                    MultiFileUtil.rename(uri, uri + "-" + fileNameNew + "-article-DEL");
                }
        );
    }

    @Override
    @Transactional
    public void deleteArticleDirByIdList(List<Integer> idList) {
        idList.forEach(
                e -> {
                    List<Map<String, Object>> mapList = multiFileMapper.selectMaps(new LambdaQueryWrapper<MultiFile>()
                            .select(MultiFile::getId, MultiFile::getUserId, MultiFile::getFileNameNew)
                            .eq(MultiFile::getFileName, e));
                    multiFileMapper.delete(new LambdaUpdateWrapper<MultiFile>()
                            .eq(MultiFile::getId, mapList.get(0).get("id"))
                            .or()
                            .eq(MultiFile::getParentId, mapList.get(0).get("id")));
                    MultiFileUtil.delete(mapList.get(0).get("user_id") + "/" + IMG_ARTICLE.getPath() + "/" + e + "-" + mapList.get(0).get("file_name_new") + "-article-DEL");
                }
        );
    }

    private void updateUserAvatarBy(Integer userId, Integer loginUserId, long fileNameOld, String fileExtension, Date updateTime) {
        long fileNameNew = IdWorker.getId();
        multiFileMapper.update(null, new LambdaUpdateWrapper<MultiFile>()
                .set(MultiFile::getFileNameNew, fileNameNew)
                .set(MultiFile::getDeletedFlag, true)
                .set(MultiFile::getHiddenFlag, true)
                .set(MultiFile::getUpdateUser, loginUserId)
                .set(MultiFile::getUpdateTime, updateTime)
                .eq(MultiFile::getFileName, fileNameOld));
        String frontPath = userId + "/" + IMG_AVATAR.getPath() + "/" + fileNameOld;
        String fullExtension = "." + fileExtension;
        MultiFileUtil.rename(frontPath + fullExtension, frontPath + "-" + fileNameNew + "-avatar-HIS" + fullExtension);
    }
}




