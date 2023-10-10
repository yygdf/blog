package com.iksling.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.iksling.blog.entity.MultiFile;
import com.iksling.blog.pojo.ArticleImgFile;

/**
 * @Entity com.iksling.blog.entity.MultiFile
 */
public interface MultiFileMapper extends BaseMapper<MultiFile> {

    ArticleImgFile selectArticleImgFileByFileName(Long fileName, Integer fileMark, Integer roleWeight, Integer userId);
}




