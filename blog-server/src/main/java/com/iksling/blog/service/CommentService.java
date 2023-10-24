package com.iksling.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.iksling.blog.dto.CommentsBackDTO;
import com.iksling.blog.entity.Comment;
import com.iksling.blog.pojo.PagePojo;
import com.iksling.blog.vo.ConditionBackVO;
import com.iksling.blog.vo.StatusBackVO;

import java.util.List;

/**
 *
 */
public interface CommentService extends IService<Comment> {
    void deleteBackCommentsByIdList(List<Integer> idList);

    void updateCommentsStatusBackVO(StatusBackVO statusBackVO);

    PagePojo<CommentsBackDTO> getCommentsBackDTO(ConditionBackVO condition);
}
