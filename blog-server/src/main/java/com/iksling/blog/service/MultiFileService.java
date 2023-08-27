package com.iksling.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.iksling.blog.entity.MultiFile;
import com.iksling.blog.vo.ArticleImageBackVO;
import com.iksling.blog.vo.UserAvatarBackVO;

/**
 *
 */
public interface MultiFileService extends IService<MultiFile> {

    String saveArticleImageBackVO(ArticleImageBackVO articleImageBackVO);

    void updateArticleImageByUrl(String url);

    String saveUserAvatarBackVO(UserAvatarBackVO userAvatarBackVO);

    void updateUserAvatarByUrl(String url);
}
