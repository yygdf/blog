package com.iksling.blog.service;

import com.iksling.blog.pojo.PagePojo;
import com.iksling.blog.dto.TagsBackDTO;
import com.iksling.blog.entity.Tag;
import com.baomidou.mybatisplus.extension.service.IService;
import com.iksling.blog.pojo.Condition;
import com.iksling.blog.vo.StatusBackVO;
import com.iksling.blog.vo.TagBackVO;

import java.util.List;

/**
 *
 */
public interface TagService extends IService<Tag> {

    void saveOrUpdateTagBackVO(TagBackVO tagBackVO);

    void deleteBackTagsByIdList(List<Integer> idList);

    void updateTagsStatusBackVO(StatusBackVO statusBackVO);

    PagePojo<TagsBackDTO> getTagsBackDTO(Condition condition);
}
