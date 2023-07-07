package com.iksling.blog.service;

import com.iksling.blog.entity.MultiFile;
import com.baomidou.mybatisplus.extension.service.IService;
import com.iksling.blog.vo.MultiFileBackVO;

/**
 *
 */
public interface MultiFileService extends IService<MultiFile> {

    void saveArticleImgInfo(MultiFileBackVO multiFileBackVO);
}
