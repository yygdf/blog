package com.iksling.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.iksling.blog.entity.MultiFile;
import com.iksling.blog.vo.ArticleImgBackVO;
import com.iksling.blog.vo.UserAvatarBackVO;

/**
 *
 */
public interface MultiFileService extends IService<MultiFile> {
    String saveArticleImgBackVO(ArticleImgBackVO articleImgBackVO);

    void updateArticleImgByFileName(String fileName);

    String saveUserAvatarBackVO(UserAvatarBackVO userAvatarBackVO);

    void updateUserAvatarByFileName(String fileName);

    void updateArticleImgBy(Integer userId, Integer articleId, String fullFileName);
}
