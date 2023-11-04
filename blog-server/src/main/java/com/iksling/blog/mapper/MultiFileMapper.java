package com.iksling.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.iksling.blog.entity.MultiFile;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @Entity com.iksling.blog.entity.MultiFile
 */
public interface MultiFileMapper extends BaseMapper<MultiFile> {
    @SuppressWarnings("MybatisXMapperMethodInspection")
    List<Map<String, Object>> selectArticleImgFileByFileName(@Param("fileNameList") List<Long> fileNameList, Integer fileMark, Integer roleWeight, Integer userId);
}




