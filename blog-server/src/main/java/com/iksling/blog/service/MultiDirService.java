package com.iksling.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.iksling.blog.entity.MultiDir;

import java.util.Date;
import java.util.List;

/**
 *
 */
public interface MultiDirService extends IService<MultiDir> {
    void saveArticleDirById(Integer id, Integer loginUserId, Date createTime);

    void updateArticleDirByIdList(List<Integer> idList, Integer loginUserId, Date updateTime);

    void deleteArticleDirByIdList(List<Integer> idList);
}
