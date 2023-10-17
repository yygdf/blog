package com.iksling.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.iksling.blog.entity.MultiDir;

import java.util.List;

/**
 *
 */
public interface MultiDirService extends IService<MultiDir> {
    void saveArticleDirById(Integer id);

    void updateArticleDirByIdList(List<Integer> idList);

    void deleteArticleDirByIdList(List<Integer> idList);
}
