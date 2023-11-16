package com.iksling.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.iksling.blog.dto.MultiFilesBackDTO;
import com.iksling.blog.entity.MultiFile;
import com.iksling.blog.vo.*;

import java.util.Date;
import java.util.List;

/**
 *
 */
public interface MultiFileService extends IService<MultiFile> {

    void saveOrUpdateMultiFileBackVO(MultiFileBackVO multiFileBackVO);

    String saveArticleImageBackVO(ArticleImageBackVO articleImageBackVO);

    String saveUserAvatarBackVO(UserAvatarBackVO userAvatarBackVO);

    void updateBackArticleImagesByFileNameList(List<Long> fileNameList);

    void updateBackUserAvatarsByFileNameList(List<Long> fileNameList);

    String updateUserAvatarVO(UserAvatarVO userAvatarVO);

    void updateArticleImageBy(Integer userId, Integer loginUserId, Integer articleId, String fullFileName, Date updateTime);

    void updateUserAvatarBy(Integer userId, Integer loginUserId, String fullFileName, Date updateTime);

    void saveArticleDirById(Integer id, Integer loginUserId, Date createTime);

    void updateArticleDirByIdList(List<Integer> idList, Integer loginUserId, Date updateTime);

    void deleteArticleDirByIdList(List<Integer> idList);

    List<MultiFilesBackDTO> getMultiFilesBackDTO(ConditionBackVO condition);

    List<MultiFilesBackDTO> getMultiFilesBackDTOById(Integer id);
}
