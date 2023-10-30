package com.iksling.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.iksling.blog.entity.MultiFile;
import com.iksling.blog.vo.ArticleImageBackVO;
import com.iksling.blog.vo.UserAvatarBackVO;

import java.util.List;

/**
 *
 */
public interface MultiFileService extends IService<MultiFile> {
    String saveArticleImageBackVO(ArticleImageBackVO articleImageBackVO);

    String saveUserAvatarBackVO(UserAvatarBackVO userAvatarBackVO);

    void updateBackArticleImagesByFileNameList(List<Long> fileNameList);

    void updateUserAvatarByFileName(Long fileName);

    void updateArticleImageBy(Integer userId, Integer articleId, String fullFileName);
}
