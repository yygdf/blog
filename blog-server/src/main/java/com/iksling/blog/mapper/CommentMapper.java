package com.iksling.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.iksling.blog.dto.CommentsBackDTO;
import com.iksling.blog.entity.Comment;
import com.iksling.blog.vo.ConditionBackVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Entity com.iksling.blog.entity.Comment
 */
public interface CommentMapper extends BaseMapper<Comment> {
    Integer selectCommentsBackDTOCount(@Param("condition") ConditionBackVO condition, Integer userId, Integer roleWeight);

    List<CommentsBackDTO> selectCommentsBackDTO(@Param("condition") ConditionBackVO condition, Integer userId, Integer roleWeight);
}




