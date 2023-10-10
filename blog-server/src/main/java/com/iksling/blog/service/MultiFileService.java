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

    void updateArticleImgByFileName(Long fileName);

    String saveUserAvatarBackVO(UserAvatarBackVO userAvatarBackVO);

    void updateUserAvatarByFileName(Long fileName);

    void updateArticleImgByUrl(String url);
}
