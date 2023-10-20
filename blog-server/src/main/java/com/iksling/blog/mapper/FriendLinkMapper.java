package com.iksling.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.iksling.blog.dto.FriendLinksBackDTO;
import com.iksling.blog.entity.FriendLink;
import com.iksling.blog.vo.ConditionBackVO;

import java.util.List;

/**
 * @Entity com.iksling.blog.entity.FriendLink
 */
public interface FriendLinkMapper extends BaseMapper<FriendLink> {
    Integer selectFriendLinksBackDTOCount(ConditionBackVO condition);

    List<FriendLinksBackDTO> selectFriendLinksBackDTO(ConditionBackVO condition);
}




