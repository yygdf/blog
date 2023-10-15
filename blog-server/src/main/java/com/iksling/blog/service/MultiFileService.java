package com.iksling.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.iksling.blog.entity.MultiFile;
import com.iksling.blog.vo.ArticleImgBackVO;
import com.iksling.blog.vo.UserAvatarBackVO;

import java.util.List;

/**
 *
 */
public interface MultiFileService extends IService<MultiFile> {
    String saveArticleImgBackVO(ArticleImgBackVO articleImgBackVO);

    void updateArticleImgByFileNameList(List<Long> fileNameList);

    String saveUserAvatarBackVO(UserAvatarBackVO userAvatarBackVO);

    void updateUserAvatarByFileName(Long fileName);

    void updateArticleImgBy(Integer userId, Integer articleId, String fullFileName);
}
