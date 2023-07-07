package com.iksling.blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iksling.blog.entity.MultiFile;
import com.iksling.blog.enums.FilePathEnum;
import com.iksling.blog.exception.FileStatusException;
import com.iksling.blog.pojo.LoginUser;
import com.iksling.blog.service.MultiFileService;
import com.iksling.blog.mapper.MultiFileMapper;
import com.iksling.blog.util.FileUploadUtil;
import com.iksling.blog.util.UserUtil;
import com.iksling.blog.vo.MultiFileBackVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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

    @Override
    public void saveArticleImgInfo(MultiFileBackVO multiFileBackVO) {
        LoginUser loginUser = UserUtil.getLoginUser();
        if (multiFileBackVO.getFile().isEmpty())
            throw new FileStatusException("文件不存在!");
        for (MultipartFile file : multiFileBackVO.getFile()) {
            if (!FileUploadUtil.checkImageFileType(file))
                throw new FileStatusException("文件类型不匹配!需要的文件类型为{.jpg .jpeg .png .gif}");
            if (!FileUploadUtil.checkFileSize(file.getSize(), FilePathEnum.ARTICLE.getSize(), FilePathEnum.ARTICLE.getUnit()))
                throw new FileStatusException("文件大小超出限制!文件最大为{" + FilePathEnum.ARTICLE.getSize() + FilePathEnum.ARTICLE.getUnit() + "}");
            String subDir = loginUser.getUserId() + "/" + multiFileBackVO.getSubDir() + "/";
            String targetAddr = FilePathEnum.ARTICLE.getPath() + subDir;
            String url = FileUploadUtil.upload(file, targetAddr);
            if (Objects.isNull(url))
                throw new FileStatusException("文件上传失败!");
            multiFileBackVO.setFile(null);
            multiFileBackVO.setFileUrl(url);
            multiFileMapper.insert(MultiFile.builder()
                .userId(loginUser.getUserId())
                .multiDirId(FilePathEnum.ARTICLE.getId())
                .subDir(subDir)
                .fileUrl(url)
                .fileDesc("用户: " + loginUser.getUsername() + ", 文章id: " + multiFileBackVO.getSubDir() + " 中的图片")
                .fileName(file.getName())
                .isHidden(false)
                .isDeleted(false)
                .ipSource(loginUser.getIpSource())
                .ipAddress(loginUser.getIpAddress())
                .createUser(loginUser.getUserId())
                .createTime(new Date())
                .build());
        }
    }
}




