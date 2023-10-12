package com.iksling.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iksling.blog.entity.Article;
import com.iksling.blog.entity.MultiDir;
import com.iksling.blog.entity.MultiFile;
import com.iksling.blog.exception.FileStatusException;
import com.iksling.blog.exception.IllegalRequestException;
import com.iksling.blog.mapper.ArticleMapper;
import com.iksling.blog.mapper.MultiDirMapper;
import com.iksling.blog.mapper.MultiFileMapper;
import com.iksling.blog.pojo.ArticleImgFile;
import com.iksling.blog.pojo.LoginUser;
import com.iksling.blog.service.MultiFileService;
import com.iksling.blog.util.MultiFileUtil;
import com.iksling.blog.util.IpUtil;
import com.iksling.blog.util.UserUtil;
import com.iksling.blog.vo.ArticleImgBackVO;
import com.iksling.blog.vo.UserAvatarBackVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

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
    private ArticleMapper articleMapper;
    @Autowired
    private MultiDirMapper multiDirMapper;

    @Resource
    private HttpServletRequest request;

    @Override
    @Transactional
    public String saveArticleImgBackVO(ArticleImgBackVO articleImgBackVO) {
        LoginUser loginUser = UserUtil.getLoginUser();
        Integer articleId = articleImgBackVO.getArticleId();
        Integer articleUserId = articleImgBackVO.getUserId();
        MultipartFile file = articleImgBackVO.getFile();
        if (file.isEmpty())
            throw new FileStatusException("文件不存在!");
        if (MultiFileUtil.checkNotValidImageFileType(file))
            throw new FileStatusException("文件类型不匹配!需要的文件类型为{.jpg .jpeg .png .gif}");
        if (MultiFileUtil.checkNotValidFileSize(file.getSize(), IMG_ARTICLE_SIZE, IMG_ARTICLE_UNIT))
            throw new FileStatusException("文件大小超出限制!文件最大为{" + IMG_ARTICLE_SIZE + IMG_ARTICLE_UNIT + "}");
        if (Objects.isNull(articleUserId))
            articleUserId = loginUser.getUserId();
        else if (loginUser.getRoleWeight() > 300 && !loginUser.getUserId().equals(articleUserId))
            throw new IllegalRequestException();
        Integer count = articleMapper.selectCount(new LambdaQueryWrapper<Article>()
                .eq(Article::getId, articleId)
                .eq(Article::getUserId, articleUserId)
                .eq(Article::getDeletedFlag, false)
                .eq(Article::getRecycleFlag, false));
        if (count != 1)
            throw new IllegalRequestException();
        long fileName = IdWorker.getId();
        String extension = getSplitStringByIndex(Objects.requireNonNull(file.getOriginalFilename()), "\\.", -1);
        String targetAddr = articleUserId + "/" + IMG_ARTICLE.getPath() + "/" + articleId;
        String url = MultiFileUtil.upload(file, targetAddr, fileName + "." + extension);
        if (Objects.isNull(url))
            throw new FileStatusException("文件上传失败!");
        articleImgBackVO.setFile(null);
        String iPAddress = IpUtil.getIpAddress(request);
        MultiDir multiDir = multiDirMapper.selectOne(new LambdaQueryWrapper<MultiDir>()
                .select(MultiDir::getId)
                .eq(MultiDir::getDirPath, articleId));
        multiFileMapper.insert(MultiFile.builder()
                .userId(articleUserId)
                .multiDirId(multiDir.getId())
                .fileDesc("用户[" + articleUserId + "], 文章[id:" + articleId + "]的图片")
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
    public void updateArticleImgByFileName(Long fileName) {
        LoginUser loginUser = UserUtil.getLoginUser();
        ArticleImgFile articleImgFile = multiFileMapper.selectArticleImgFileByFileName(fileName, IMG_ARTICLE.getMark(), loginUser.getRoleWeight(), loginUser.getUserId());
        if (Objects.isNull(articleImgFile))
            return;
        long fileNameNew = IdWorker.getId();
        multiFileMapper.update(null, new LambdaUpdateWrapper<MultiFile>()
                .set(MultiFile::getFileNameNew, fileNameNew)
                .set(MultiFile::getDeletedFlag, true)
                .set(MultiFile::getUpdateUser, loginUser.getUserId())
                .set(MultiFile::getUpdateTime, new Date())
                .eq(MultiFile::getId, articleImgFile.getId()));
        String frontPath = articleImgFile.getUserId() + "/" + IMG_ARTICLE.getPath() + "/" + articleImgFile.getDirPath() + "/" + fileName;
        String fullExtension = "." + articleImgFile.getFileExtension();
        MultiFileUtil.rename(frontPath + fullExtension, frontPath + "-" + fileNameNew + "-del" + fullExtension);
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
        if (Objects.isNull(userId))
            userId = loginUser.getUserId();
        else if (loginUser.getRoleWeight() > 200 && !loginUser.getUserId().equals(userId))
            throw new IllegalRequestException();
        long fileName = IdWorker.getId();
        String extension = getSplitStringByIndex(Objects.requireNonNull(file.getOriginalFilename()), "\\.", -1);
        String targetAddr = userId + "/" + IMG_AVATAR.getPath();
        String url = MultiFileUtil.upload(file, targetAddr, fileName + "." + extension);
        if (Objects.isNull(url))
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
                .fileDesc("用户[" + userId + "]的头像")
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
    public void updateUserAvatarByFileName(Long fileName) {
        LoginUser loginUser = UserUtil.getLoginUser();
        List<Map<String, Object>> multiFileMap = multiFileMapper.selectMaps(new QueryWrapper<MultiFile>()
                .select("id", "user_id", "file_extension")
                .eq("file_name", fileName)
                .eq("file_mark", IMG_AVATAR.getMark())
                .eq(loginUser.getRoleWeight() > 200, "user_id", loginUser.getUserId())
                .eq("deleted_flag", false));
        if (CollectionUtils.isEmpty(multiFileMap))
            return;
        long fileNameNew = IdWorker.getId();
        multiFileMapper.update(null, new LambdaUpdateWrapper<MultiFile>()
                .set(MultiFile::getFileNameNew, fileNameNew)
                .set(MultiFile::getDeletedFlag, true)
                .set(MultiFile::getUpdateUser, loginUser.getUserId())
                .set(MultiFile::getUpdateTime, new Date())
                .eq(MultiFile::getId, multiFileMap.get(0).get("id")));
        String frontPath = multiFileMap.get(0).get("user_id") + "/" + IMG_AVATAR.getPath() + "/" + fileName;
        String fullExtension = "." + multiFileMap.get(0).get("file_extension");
        MultiFileUtil.rename(frontPath + fullExtension, frontPath + "-" + fileNameNew + "-del" + fullExtension);
    }

    @Override
    public void updateArticleImgBy(Integer userId, Integer articleId, String fullFileName) {
        String[] fileNameArr = fullFileName.split("\\.");
        long fileNameNew = IdWorker.getId();
        long fileNameOld = Long.parseLong(fileNameArr[0]);
        LoginUser loginUser = UserUtil.getLoginUser();
        int count = multiFileMapper.update(null, new LambdaUpdateWrapper<MultiFile>()
                .set(MultiFile::getFileNameNew, fileNameNew)
                .set(MultiFile::getDeletedFlag, true)
                .set(MultiFile::getUpdateUser, loginUser.getUserId())
                .set(MultiFile::getUpdateTime, new Date())
                .eq(MultiFile::getFileName, fileNameOld)
                .eq(loginUser.getRoleWeight() > 300, MultiFile::getUserId, loginUser.getUserId())
                .eq(MultiFile::getDeletedFlag, false));
        if (count == 0)
            return;
        String frontPath = userId + "/" + IMG_ARTICLE.getPath() + "/" + articleId + "/" + fileNameOld;
        String fullExtension = "." + fileNameArr[1];
        MultiFileUtil.rename(frontPath + fullExtension, frontPath + fileNameOld + "-" + fileNameNew + "-del" + fullExtension);
    }
}




