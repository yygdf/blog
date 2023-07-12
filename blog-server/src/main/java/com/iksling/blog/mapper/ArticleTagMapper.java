package com.iksling.blog.mapper;

import com.iksling.blog.entity.ArticleTag;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Entity com.iksling.blog.entity.ArticleTag
 */
public interface ArticleTagMapper extends BaseMapper<ArticleTag> {

    void deleteByTagIdList(@Param("tagIdList") List<Integer> tagIdList);
}




