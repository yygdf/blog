package com.iksling.blog.service;

import com.iksling.blog.dto.CommentsBackDTO;
import com.iksling.blog.entity.Comment;
import com.baomidou.mybatisplus.extension.service.IService;
import com.iksling.blog.pojo.PagePojo;
import com.iksling.blog.vo.ConditionVO;
import com.iksling.blog.vo.UpdateBatchVO;

import java.util.List;

/**
 *
 */
public interface CommentService extends IService<Comment> {
    PagePojo<CommentsBackDTO> getPageCommentsBackDTO(ConditionVO condition);

    void updateCommentsStatus(UpdateBatchVO updateBatchVO);

    void deleteCommentIdList(List<Integer> commentIdList);
}
