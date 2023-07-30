package com.iksling.blog.mapper;

import com.iksling.blog.dto.CommentsBackDTO;
import com.iksling.blog.entity.Comment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.iksling.blog.vo.ConditionVO;
import com.iksling.blog.vo.UpdateBatchVO;

import java.util.List;

/**
 * @Entity com.iksling.blog.entity.Comment
 */
public interface CommentMapper extends BaseMapper<Comment> {

    Integer selectCountByCondition(ConditionVO condition, Integer userId, Integer roleWeight);

    List<CommentsBackDTO> listCommentsBackDTO(ConditionVO condition, Integer userId, Integer roleWeight);

    Integer updateCommentsGarbageVO(UpdateBatchVO garbage, Integer userId, Integer roleWeight);
}




