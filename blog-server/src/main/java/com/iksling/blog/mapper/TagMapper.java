package com.iksling.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.iksling.blog.dto.TagsBackDTO;
import com.iksling.blog.entity.Tag;
import com.iksling.blog.vo.ConditionBackVO;

import java.util.List;

/**
 * @Entity com.iksling.blog.entity.Tag
 */
public interface TagMapper extends BaseMapper<Tag> {
    Integer selectTagsBackDTOCount(ConditionBackVO condition, Integer userId, Integer roleWeight);

    List<TagsBackDTO> selectTagsBackDTO(ConditionBackVO condition, Integer userId, Integer roleWeight);
}




