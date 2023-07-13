package com.iksling.blog.service;

import com.iksling.blog.pojo.PagePojo;
import com.iksling.blog.dto.TagsBackDTO;
import com.iksling.blog.entity.Tag;
import com.baomidou.mybatisplus.extension.service.IService;
import com.iksling.blog.vo.ConditionVO;
import com.iksling.blog.vo.TagBackVO;

import java.util.List;

/**
 *
 */
public interface TagService extends IService<Tag> {

    PagePojo<TagsBackDTO> getPageTagsBackDTO(ConditionVO condition);

    void deleteTagIdList(List<Integer> tagIdList);

    void saveOrUpdateTagBackVO(TagBackVO tagBackVO);
}
