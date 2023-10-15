package com.iksling.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.iksling.blog.entity.MultiFile;

import java.util.Map;

/**
 * @Entity com.iksling.blog.entity.MultiFile
 */
public interface MultiFileMapper extends BaseMapper<MultiFile> {
    @SuppressWarnings("MybatisXMapperMethodInspection")
    Map<String, Object> selectArticleImgFileByFileName(Long fileName, Integer fileMark, Integer roleWeight, Integer userId);
}




