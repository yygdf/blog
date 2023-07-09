package com.iksling.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.iksling.blog.entity.MultiFile;
import com.iksling.blog.vo.MultiFileArticleBackVO;

/**
 *
 */
public interface MultiFileService extends IService<MultiFile> {

    String saveMultiFileArticleBackVO(MultiFileArticleBackVO multiFileArticleBackVO);

    void deleteArticleImageByUrl(String url);
}
