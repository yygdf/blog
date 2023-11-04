package com.iksling.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iksling.blog.entity.Article;
import com.iksling.blog.entity.MultiDir;
import com.iksling.blog.entity.MultiFile;
import com.iksling.blog.entity.UserAuth;
import com.iksling.blog.exception.FileStatusException;
import com.iksling.blog.exception.OperationStatusException;
import com.iksling.blog.mapper.ArticleMapper;
import com.iksling.blog.mapper.MultiDirMapper;
import com.iksling.blog.mapper.MultiFileMapper;
import com.iksling.blog.mapper.UserAuthMapper;
import com.iksling.blog.pojo.LoginUser;
import com.iksling.blog.service.MultiFileService;
import com.iksling.blog.util.IpUtil;
import com.iksling.blog.util.MultiFileUtil;
import com.iksling.blog.util.UserUtil;
import com.iksling.blog.vo.ArticleImageBackVO;
import com.iksling.blog.vo.UserAvatarBackVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.iksling.blog.constant.CommonConst.*;
import static com.iksling.blog.enums.FilePathEnum.IMG_ARTICLE;
import static com.iksling.blog.enums.FilePathEnum.IMG_AVATAR;
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
    private UserAuthMapper userAuthMapper;
    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private MultiDirMapper multiDirMapper;

    @Resource
    private HttpServletRequest request;

    @Override
    @Transactional
    public String saveArticleImageBackVO(ArticleImageBackVO articleImageBackVO) {
        LoginUser loginUser = UserUtil.getLoginUser();
        Integer articleId = articleImageBackVO.getArticleId();
        Integer articleUserId = articleImageBackVO.getUserId();
        MultipartFile file = articleImageBackVO.getFile();
        if (file.isEmpty())
            throw new FileStatusException("文件不存在!");
        if (MultiFileUtil.checkNotValidImageFileType(file))
            throw new FileStatusException("文件类型不匹配!需要的文件类型为{.jpg .jpeg .png .gif}");
        if (MultiFileUtil.checkNotValidFileSize(file.getSize(), IMG_ARTICLE_SIZE, IMG_ARTICLE_UNIT))
            throw new FileStatusException("文件大小超出限制!文件最大为{" + IMG_ARTICLE_SIZE + IMG_ARTICLE_UNIT + "}");
        if (articleUserId == null)
            articleUserId = loginUser.getUserId();
        else if (loginUser.getRoleWeight() > 300 && !loginUser.getUserId().equals(articleUserId))
            throw new OperationStatusException();
        Integer count = articleMapper.selectCount(new LambdaQueryWrapper<Article>()
                .eq(Article::getId, articleId)
                .eq(Article::getUserId, articleUserId)
                .eq(Article::getDeletedFlag, false)
                .eq(Article::getRecycleFlag, false));
        if (count != 1)
            throw new OperationStatusException();
        long fileName = IdWorker.getId();
        String extension = getSplitStringByIndex(Objects.requireNonNull(file.getOriginalFilename()), "\\.", -1);
        String targetAddr = articleUserId + "/" + IMG_ARTICLE.getPath() + "/" + articleId;
        String url = MultiFileUtil.upload(file, targetAddr, fileName + "." + extension);
        if (url == null)
            throw new FileStatusException("文件上传失败!");
        articleImageBackVO.setFile(null);
        String iPAddress = IpUtil.getIpAddress(request);
        MultiDir multiDir = multiDirMapper.selectOne(new LambdaQueryWrapper<MultiDir>()
                .select(MultiDir::getId)
                .eq(MultiDir::getDirPath, articleId));
        multiFileMapper.insert(MultiFile.builder()
                .userId(articleUserId)
                .multiDirId(multiDir.getId())
                .fileDesc("{'userId':"+articleUserId+",'articleId':"+articleId+"}")
                .fileMark(IMG_ARTICLE.getMark())
                .fileName(fileName)
                .fileExtension(extension)
                .fileNameOrigin(file.getOriginalFilename())
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
        LoginUser loginUser = UserUtil.getLoginUser();
        Integer userId = userAvatarBackVO.getUserId();
        MultipartFile file = userAvatarBackVO.getFile();
        if (file.isEmpty())
            throw new FileStatusException("文件不存在!");
        if (MultiFileUtil.checkNotValidImageFileType(file))
            throw new FileStatusException("文件类型不匹配!需要的文件类型为{.jpg .jpeg .png .gif}");
        if (MultiFileUtil.checkNotValidFileSize(file.getSize(), IMG_AVATAR_SIZE, IMG_AVATAR_UNIT))
            throw new FileStatusException("文件大小超出限制!文件最大为{" + IMG_AVATAR_SIZE + IMG_AVATAR_UNIT + "}");
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
        String extension = getSplitStringByIndex(Objects.requireNonNull(file.getOriginalFilename()), "\\.", -1);
        String targetAddr = userId + "/" + IMG_AVATAR.getPath();
        String url = MultiFileUtil.upload(file, targetAddr, fileName + "." + extension);
        if (url == null)
            throw new FileStatusException("文件上传失败!");
        userAvatarBackVO.setFile(null);
        MultiDir multiDir = multiDirMapper.selectOne(new LambdaQueryWrapper<MultiDir>()
                .select(MultiDir::getId)
                .eq(MultiDir::getUserId, userId)
                .eq(MultiDir::getDirPath, IMG_AVATAR.getMark()));
        String iPAddress = IpUtil.getIpAddress(request);
        multiFileMapper.insert(MultiFile.builder()
                .userId(userId)
                .multiDirId(multiDir.getId())
                .fileDesc("{'userId':"+userId+"}")
                .fileMark(IMG_AVATAR.getMark())
                .fileName(fileName)
                .fileExtension(extension)
                .fileNameOrigin(file.getOriginalFilename())
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
        List<Map<String, Object>> mapList = multiFileMapper.selectArticleImgFileByFileName(fileNameList, IMG_ARTICLE.getMark(), loginUser.getRoleWeight(), loginUser.getUserId());
        if (mapList.isEmpty())
            throw new OperationStatusException();
        mapList.forEach(e -> {
            long fileNameNew = IdWorker.getId();
            multiFileMapper.update(null, new LambdaUpdateWrapper<MultiFile>()
                    .set(MultiFile::getFileNameNew, fileNameNew)
                    .set(MultiFile::getDeletedFlag, true)
                    .set(MultiFile::getUpdateUser, loginUser.getUserId())
                    .set(MultiFile::getUpdateTime, new Date())
                    .eq(MultiFile::getId, e.get("id")));
            String frontPath = e.get("user_id") + "/" + IMG_ARTICLE.getPath() + "/" + e.get("dir_path") + "/" + e.get("file_name");
            String fullExtension = "." + e.get("file_extension");
            MultiFileUtil.rename(frontPath + fullExtension, frontPath + "-" + fileNameNew + "-article-del" + fullExtension);
        });
    }

    @Override
    @Transactional
    public void updateBackUserAvatarsByFileNameList(List<Long> fileNameList) {
        LoginUser loginUser = UserUtil.getLoginUser();
        List<Map<String, Object>> mapList = multiFileMapper.selectMaps(new QueryWrapper<MultiFile>()
                .select("id", "user_id", "file_name", "file_extension")
                .in("file_name", fileNameList)
                .eq("file_mark", IMG_AVATAR.getMark())
                .eq(loginUser.getRoleWeight() > 200, "user_id", loginUser.getUserId())
                .eq("deleted_flag", false));
        if (mapList.isEmpty())
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
            MultiFileUtil.rename(frontPath + fullExtension, frontPath + "-" + fileNameNew + "-avatar-del" + fullExtension);
        });
    }

    @Override
    public void updateArticleImageBy(Integer userId, Integer articleId, String fullFileName) {
        String[] fileNameArr = fullFileName.split("\\.");
        long fileNameNew = IdWorker.getId();
        long fileNameOld = Long.parseLong(fileNameArr[0]);
        LoginUser loginUser = UserUtil.getLoginUser();
        multiFileMapper.update(null, new LambdaUpdateWrapper<MultiFile>()
                .set(MultiFile::getFileNameNew, fileNameNew)
                .set(MultiFile::getDeletedFlag, true)
                .set(MultiFile::getUpdateUser, loginUser.getUserId())
                .set(MultiFile::getUpdateTime, new Date())
                .eq(MultiFile::getFileName, fileNameOld));
        String frontPath = userId + "/" + IMG_ARTICLE.getPath() + "/" + articleId + "/" + fileNameOld;
        String fullExtension = "." + fileNameArr[1];
        MultiFileUtil.rename(frontPath + fullExtension, frontPath + fileNameOld + "-" + fileNameNew + "-article-del" + fullExtension);
    }

    @Override
    public void updateUserAvatarBy(Integer userId, String fullFileName) {
        String[] fileNameArr = fullFileName.split("\\.");
        long fileNameNew = IdWorker.getId();
        long fileNameOld = Long.parseLong(fileNameArr[0]);
        LoginUser loginUser = UserUtil.getLoginUser();
        multiFileMapper.update(null, new LambdaUpdateWrapper<MultiFile>()
                .set(MultiFile::getFileNameNew, fileNameNew)
                .set(MultiFile::getDeletedFlag, true)
                .set(MultiFile::getUpdateUser, loginUser.getUserId())
                .set(MultiFile::getUpdateTime, new Date())
                .setSql("file_name_origin = CONCAT(file_name_origin,'-his')")
                .eq(MultiFile::getFileName, fileNameOld));
        String frontPath = userId + "/" + IMG_AVATAR.getPath() + "/" + fileNameOld;
        String fullExtension = "." + fileNameArr[1];
        MultiFileUtil.rename(frontPath + fullExtension, frontPath + fileNameOld + "-" + fileNameNew + "-avatar-his" + fullExtension);
    }
}




