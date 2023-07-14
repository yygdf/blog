package com.iksling.blog.mapper;

import com.iksling.blog.entity.Tag;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * @Entity com.iksling.blog.entity.Tag
 */
public interface TagMapper extends BaseMapper<Tag> {

    Integer deleteTagIdList(List<Integer> tagIdList, Integer userId);
}




