package com.iksling.blog.mapper;

import com.iksling.blog.dto.FriendLinksBackDTO;
import com.iksling.blog.entity.FriendLink;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.iksling.blog.vo.ConditionVO;
import com.iksling.blog.vo.UpdateBatchVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Entity com.iksling.blog.entity.FriendLink
 */
public interface FriendLinkMapper extends BaseMapper<FriendLink> {

    List<FriendLinksBackDTO> listFriendLinksBackDTO(@Param("condition") ConditionVO condition, Integer userId);

    Integer updateFriendLinksStatus(@Param("updateBatch") UpdateBatchVO updateBatch, Integer roleWeight);
}




