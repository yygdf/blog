package com.iksling.blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iksling.blog.dto.FriendLinksBackDTO;
import com.iksling.blog.entity.FriendLink;
import com.iksling.blog.pojo.PagePojo;
import com.iksling.blog.service.FriendLinkService;
import com.iksling.blog.mapper.FriendLinkMapper;
import com.iksling.blog.vo.ConditionVO;
import com.iksling.blog.vo.FriendLinkBackVO;
import com.iksling.blog.vo.UpdateBatchVO;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 */
@Service
public class FriendLinkServiceImpl extends ServiceImpl<FriendLinkMapper, FriendLink>
    implements FriendLinkService{

    @Override
    public PagePojo<FriendLinksBackDTO> getPageFriendLinksBackDTO(ConditionVO condition) {

        return null;
    }

    @Override
    public void updateFriendLinksStatus(UpdateBatchVO updateBatchVO) {

    }

    @Override
    public void deleteFriendLinkIdList(List<Integer> friendLinkIdList) {

    }

    @Override
    public void saveOrUpdateFriendLinkBackVO(FriendLinkBackVO friendLinkBackVO) {

    }
}




