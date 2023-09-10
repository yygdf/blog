package com.iksling.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iksling.blog.entity.Article;
import com.iksling.blog.entity.MultiFile;
import com.iksling.blog.enums.FilePathEnum;
import com.iksling.blog.exception.FileStatusException;
import com.iksling.blog.exception.IllegalRequestException;
import com.iksling.blog.mapper.ArticleMapper;
import com.iksling.blog.mapper.MultiFileMapper;
import com.iksling.blog.pojo.LoginUser;
import com.iksling.blog.service.MultiFileService;
import com.iksling.blog.util.FileUploadUtil;
import com.iksling.blog.util.IpUtil;
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
import java.util.Objects;

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

    @Resource
    private HttpServletRequest request;

    @Override
    @Transactional
    public String saveArticleImageBackVO(ArticleImageBackVO articleImageBackVO) {
        LoginUser loginUser = UserUtil.getLoginUser();
        Integer articleUserId = articleImageBackVO.getUserId();
        if (articleImageBackVO.getFile().isEmpty())
            throw new FileStatusException("文件不存在!");
        MultipartFile file = articleImageBackVO.getFile();
        if (!FileUploadUtil.checkImageFileType(file))
            throw new FileStatusException("文件类型不匹配!需要的文件类型为{.jpg .jpeg .png .gif}");
        if (!FileUploadUtil.checkFileSize(file.getSize(), FilePathEnum.ARTICLE.getSize(), FilePathEnum.ARTICLE.getUnit()))
            throw new FileStatusException("文件大小超出限制!文件最大为{" + FilePathEnum.ARTICLE.getSize() + FilePathEnum.ARTICLE.getUnit() + "}");
        if (Objects.nonNull(articleUserId) && loginUser.getRoleWeight() > 300 && !loginUser.getUserId().equals(articleUserId))
            throw new IllegalRequestException();
        Integer count;
        try {
            count = articleMapper.selectCount(new LambdaQueryWrapper<Article>()
                    .eq(Article::getId, Integer.parseInt(articleImageBackVO.getFileSubDir()))
                    .eq(Objects.isNull(articleUserId), Article::getUserId, loginUser.getUserId())
                    .eq(Objects.nonNull(articleUserId), Article::getUserId, articleUserId));
        } catch (NumberFormatException e) {
            throw new IllegalRequestException();
        }
        if (count != 1)
            throw new IllegalRequestException();
        if (Objects.isNull(articleUserId))
            articleUserId = loginUser.getUserId();
        String fireSubDir = articleUserId + "/" + articleImageBackVO.getFileSubDir() + "/";
        String targetAddr = FilePathEnum.ARTICLE.getPath() + fireSubDir;
        String url = FileUploadUtil.upload(file, targetAddr);
        if (Objects.isNull(url))
            throw new FileStatusException("文件上传失败!");
        articleImageBackVO.setFile(null);
        String iPAddress = IpUtil.getIpAddress(request);
        multiFileMapper.insert(MultiFile.builder()
                .userId(articleUserId)
                .multiDirId(FilePathEnum.ARTICLE.getId())
                .fileUrl(url)
                .fileDesc("用户[" + articleUserId + "], 文章id[" + articleImageBackVO.getFileSubDir() + "]中的插图")
                .fileName(file.getOriginalFilename())
                .fileSubDir(fireSubDir)
                .hiddenFlag(false)
                .deletedFlag(false)
                .ipAddress(iPAddress)
                .ipSource(IpUtil.getIpSource(iPAddress))
                .createUser(loginUser.getUserId())
                .createTime(new Date())
                .build());
        return url;
    }

    @Override
    @Transactional
    public void updateArticleImageByUrl(String url) {
        LoginUser loginUser = UserUtil.getLoginUser();
        multiFileMapper.update(null, new LambdaUpdateWrapper<MultiFile>()
                .set(MultiFile::getDeletedFlag, true)
                .eq(MultiFile::getFileUrl, url)
                .eq(loginUser.getRoleWeight() > 300, MultiFile::getUserId, loginUser.getUserId()));
    }

    @Override
    @Transactional
    public String saveUserAvatarBackVO(UserAvatarBackVO userAvatarBackVO) {
        LoginUser loginUser = UserUtil.getLoginUser();
        Integer userId = userAvatarBackVO.getUserId();
        if (userAvatarBackVO.getFile().isEmpty())
            throw new FileStatusException("文件不存在!");
        MultipartFile file = userAvatarBackVO.getFile();
        if (!FileUploadUtil.checkImageFileType(file))
            throw new FileStatusException("文件类型不匹配!需要的文件类型为{.jpg .jpeg .png .gif}");
        if (!FileUploadUtil.checkFileSize(file.getSize(), FilePathEnum.AVATAR.getSize(), FilePathEnum.AVATAR.getUnit()))
            throw new FileStatusException("文件大小超出限制!文件最大为{" + FilePathEnum.AVATAR.getSize() + FilePathEnum.AVATAR.getUnit() + "}");
        String fireSubDir = userId + "/";
        String targetAddr = FilePathEnum.AVATAR.getPath() + fireSubDir;
        String url = FileUploadUtil.upload(file, targetAddr);
        if (Objects.isNull(url))
            throw new FileStatusException("文件上传失败!");
        userAvatarBackVO.setFile(null);
        String iPAddress = IpUtil.getIpAddress(request);
        multiFileMapper.insert(MultiFile.builder()
                .userId(userId)
                .multiDirId(FilePathEnum.AVATAR.getId())
                .fileUrl(url)
                .fileDesc("用户[" + userId + "]的头像")
                .fileName(file.getOriginalFilename())
                .fileSubDir(fireSubDir)
                .hiddenFlag(false)
                .deletedFlag(false)
                .ipAddress(iPAddress)
                .ipSource(IpUtil.getIpSource(iPAddress))
                .createUser(loginUser.getUserId())
                .createTime(new Date())
                .build());
        return url;
    }

    @Override
    @Transactional
    public void updateUserAvatarByUrl(String url) {
        LoginUser loginUser = UserUtil.getLoginUser();
        multiFileMapper.update(null, new LambdaUpdateWrapper<MultiFile>()
                .set(MultiFile::getDeletedFlag, true)
                .eq(MultiFile::getFileUrl, url)
                .eq(loginUser.getRoleWeight() > 200, MultiFile::getUserId, loginUser.getUserId()));
    }
}




