package com.iksling.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.iksling.blog.dto.FriendLinksBackDTO;
import com.iksling.blog.entity.FriendLink;
import com.iksling.blog.vo.ConditionBackVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Entity com.iksling.blog.entity.FriendLink
 */
public interface FriendLinkMapper extends BaseMapper<FriendLink> {
    Integer selectFriendLinksBackDTOCount(@Param("condition") ConditionBackVO condition);

    List<FriendLinksBackDTO> selectFriendLinksBackDTO(@Param("condition") ConditionBackVO condition);
}




