package com.iksling.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.iksling.blog.dto.CommentsBackDTO;
import com.iksling.blog.dto.CommentsDTO;
import com.iksling.blog.entity.Comment;
import com.iksling.blog.pojo.Condition;
import com.iksling.blog.pojo.PagePojo;
import com.iksling.blog.vo.CommentVO;
import com.iksling.blog.vo.StatusBackVO;

import java.util.List;

/**
 *
 */
public interface CommentService extends IService<Comment> {
    void deleteBackCommentsByIdList(List<Integer> idList);

    void updateCommentsStatusBackVO(StatusBackVO statusBackVO);

    PagePojo<CommentsBackDTO> getCommentsBackDTO(Condition condition);

    void saveCommentVO(CommentVO commentVO);

    void saveCommentLike(Integer id);

    PagePojo<CommentsDTO> getCommentsDTO(Condition condition);
}
