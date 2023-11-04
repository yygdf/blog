package com.iksling.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.iksling.blog.dto.TagsBackDTO;
import com.iksling.blog.entity.Tag;
import com.iksling.blog.vo.ConditionBackVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Entity com.iksling.blog.entity.Tag
 */
public interface TagMapper extends BaseMapper<Tag> {
    Integer selectTagsBackDTOCount(@Param("condition") ConditionBackVO condition, Integer userId, Integer roleWeight);

    List<TagsBackDTO> selectTagsBackDTO(@Param("condition") ConditionBackVO condition, Integer userId, Integer roleWeight);
}




