package com.iksling.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.iksling.blog.dto.CommentsBackDTO;
import com.iksling.blog.dto.CommentsDTO;
import com.iksling.blog.dto.CommentsReplyDTO;
import com.iksling.blog.entity.Comment;
import com.iksling.blog.pojo.Condition;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Entity com.iksling.blog.entity.Comment
 */
public interface CommentMapper extends BaseMapper<Comment> {
    Integer selectCommentsBackDTOCount(@Param("condition") Condition condition, Integer userId, Integer roleWeight);

    List<CommentsBackDTO> selectCommentsBackDTO(@Param("condition") Condition condition, Integer userId, Integer roleWeight);

    Integer selectCommentsDTOCount(@Param("condition") Condition condition);

    List<CommentsDTO> selectCommentsDTO(@Param("condition") Condition condition);

    List<CommentsReplyDTO> selectCommentsReplyDTO(@Param("commentsDTOIdList") List<Integer> commentsDTOIdList);
}




