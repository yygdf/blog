package com.iksling.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.iksling.blog.dto.FriendLinksBackDTO;
import com.iksling.blog.entity.FriendLink;
import com.iksling.blog.pojo.PagePojo;
import com.iksling.blog.vo.ConditionBackVO;
import com.iksling.blog.vo.FriendLinkBackVO;
import com.iksling.blog.vo.UpdateBatchVO;

import java.util.List;

/**
 *
 */
public interface FriendLinkService extends IService<FriendLink> {

    PagePojo<FriendLinksBackDTO> getPageFriendLinksBackDTO(ConditionBackVO condition);

    void updateFriendLinksStatus(UpdateBatchVO updateBatchVO);

    void deleteFriendLinkIdList(List<Integer> friendLinkIdList);

    void saveOrUpdateFriendLinkBackVO(FriendLinkBackVO friendLinkBackVO);
}
