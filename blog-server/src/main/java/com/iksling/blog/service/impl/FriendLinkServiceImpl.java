package com.iksling.blog.service.impl;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iksling.blog.dto.FriendLinksBackDTO;
import com.iksling.blog.entity.FriendLink;
import com.iksling.blog.exception.IllegalRequestException;
import com.iksling.blog.mapper.FriendLinkMapper;
import com.iksling.blog.pojo.LoginUser;
import com.iksling.blog.pojo.PagePojo;
import com.iksling.blog.service.FriendLinkService;
import com.iksling.blog.util.UserUtil;
import com.iksling.blog.vo.ConditionVO;
import com.iksling.blog.vo.FriendLinkBackVO;
import com.iksling.blog.vo.UpdateBatchVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 *
 */
@Service
public class FriendLinkServiceImpl extends ServiceImpl<FriendLinkMapper, FriendLink>
    implements FriendLinkService{
    @Autowired
    private FriendLinkMapper friendLinkMapper;

    @Override
    public PagePojo<FriendLinksBackDTO> getPageFriendLinksBackDTO(ConditionVO condition) {
        if (UserUtil.getLoginUser().getRoleWeight() > 100 && Objects.equals(condition.getDeletedFlag(), true))
            throw new IllegalRequestException();
        if (Objects.nonNull(condition.getKeywords()))
            condition.setKeywords(condition.getKeywords().trim());
        Integer count = friendLinkMapper.selectFriendLinksBackDTOCount(condition);
        if (count == 0)
            return new PagePojo<>();
        condition.setCurrent((condition.getCurrent() - 1) * condition.getSize());
        List<FriendLinksBackDTO> friendLinksBackDTOList = friendLinkMapper.listFriendLinksBackDTO(condition);
        return new PagePojo<>(count, friendLinksBackDTOList);
    }

    @Override
    @Transactional
    public void updateFriendLinksStatus(UpdateBatchVO updateBatchVO) {
        if (UserUtil.getLoginUser().getRoleWeight() > 100 && !updateBatchVO.getDeletedFlag())
            throw new IllegalRequestException();
        Integer count = friendLinkMapper.updateFriendLinksStatus(updateBatchVO);
        if (count != updateBatchVO.getIdList().size())
            throw new IllegalRequestException();
    }

    @Override
    @Transactional
    public void deleteFriendLinkIdList(List<Integer> friendLinkIdList) {
        if (CollectionUtils.isEmpty(friendLinkIdList))
            throw new IllegalRequestException();
        int count = friendLinkMapper.deleteBatchIds(friendLinkIdList);
        if (count != friendLinkIdList.size())
            throw new IllegalRequestException();
    }

    @Override
    @Transactional
    public void saveOrUpdateFriendLinkBackVO(FriendLinkBackVO friendLinkBackVO) {
        LoginUser loginUser = UserUtil.getLoginUser();
        FriendLink friendLink = FriendLink.builder()
                .id(friendLinkBackVO.getId())
                .userId(friendLinkBackVO.getUserId())
                .linkUrl(friendLinkBackVO.getLinkUrl().trim())
                .linkDesc(friendLinkBackVO.getLinkDesc().trim())
                .linkLogo(friendLinkBackVO.getLinkLogo().trim())
                .linkName(friendLinkBackVO.getLinkName().trim())
                .build();
        if (Objects.isNull(friendLinkBackVO.getId())) {
            friendLink.setCreateUser(loginUser.getUserId());
            friendLink.setCreateTime(new Date());
        } else {
            friendLink.setUpdateUser(loginUser.getUserId());
            friendLink.setUpdateTime(new Date());
        }
        this.saveOrUpdate(friendLink);
    }
}




