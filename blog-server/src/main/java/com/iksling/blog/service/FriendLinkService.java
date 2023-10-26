package com.iksling.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.iksling.blog.dto.FriendLinksBackDTO;
import com.iksling.blog.entity.FriendLink;
import com.iksling.blog.pojo.PagePojo;
import com.iksling.blog.vo.ConditionBackVO;
import com.iksling.blog.vo.FriendLinkBackVO;
import com.iksling.blog.vo.StatusBackVO;

import java.util.List;

/**
 *
 */
public interface FriendLinkService extends IService<FriendLink> {

    void saveOrUpdateFriendLinkBackVO(FriendLinkBackVO friendLinkBackVO);

    void deleteBackFriendLinksByIdList(List<Integer> idList);

    void updateFriendLinksStatusBackVO(StatusBackVO statusBackVO);

    PagePojo<FriendLinksBackDTO> getFriendLinksBackDTO(ConditionBackVO condition);
}
