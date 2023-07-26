package com.iksling.blog.service;

import com.iksling.blog.dto.CommentsBackDTO;
import com.iksling.blog.entity.Comment;
import com.baomidou.mybatisplus.extension.service.IService;
import com.iksling.blog.pojo.PagePojo;
import com.iksling.blog.vo.ConditionVO;
import com.iksling.blog.vo.GarbageVO;

import java.util.List;

/**
 *
 */
public interface CommentService extends IService<Comment> {
    PagePojo<CommentsBackDTO> getPageCommentsBackDTO(ConditionVO condition);

    void updateCommentsGarbageVO(GarbageVO garbageVO);

    void deleteCommentIdList(List<Integer> commentIdList);
}
