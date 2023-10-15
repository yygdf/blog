package com.iksling.blog.mapper;

import com.iksling.blog.dto.CommentsBackDTO;
import com.iksling.blog.entity.Comment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.iksling.blog.vo.ConditionBackVO;
import com.iksling.blog.vo.UpdateBatchVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Entity com.iksling.blog.entity.Comment
 */
public interface CommentMapper extends BaseMapper<Comment> {

    List<CommentsBackDTO> listCommentsBackDTO(@Param("condition") ConditionBackVO condition, Integer userId, Integer roleWeight);

    Integer updateCommentsStatus(@Param("updateBatch") UpdateBatchVO updateBatch, Integer userId, Integer roleWeight);

    Integer selectCommentsBackDTOCount(@Param("condition") ConditionBackVO condition, Integer userId, Integer roleWeight);
}




