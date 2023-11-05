package com.iksling.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.iksling.blog.entity.MultiFile;
import com.iksling.blog.vo.ArticleImageBackVO;
import com.iksling.blog.vo.UserAvatarBackVO;
import com.iksling.blog.vo.UserAvatarVO;

import java.util.Date;
import java.util.List;

/**
 *
 */
public interface MultiFileService extends IService<MultiFile> {
    String saveArticleImageBackVO(ArticleImageBackVO articleImageBackVO);

    String saveUserAvatarBackVO(UserAvatarBackVO userAvatarBackVO);

    void updateBackArticleImagesByFileNameList(List<Long> fileNameList);

    void updateBackUserAvatarsByFileNameList(List<Long> fileNameList);

    String updateUserAvatarVO(UserAvatarVO userAvatarVO);

    void updateArticleImageBy(Integer userId, Integer loginUserId, Integer articleId, String fullFileName, Date updateTime);

    void updateUserAvatarBy(Integer userId, Integer loginUserId, String fullFileName, Date updateTime);
}
