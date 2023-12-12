package com.iksling.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iksling.blog.dto.FriendLinksBackDTO;
import com.iksling.blog.entity.FriendLink;
import com.iksling.blog.exception.IllegalRequestException;
import com.iksling.blog.exception.OperationStatusException;
import com.iksling.blog.mapper.FriendLinkMapper;
import com.iksling.blog.pojo.LoginUser;
import com.iksling.blog.pojo.PagePojo;
import com.iksling.blog.service.FriendLinkService;
import com.iksling.blog.util.BeanCopyUtil;
import com.iksling.blog.util.UserUtil;
import com.iksling.blog.vo.ConditionBackVO;
import com.iksling.blog.vo.FriendLinkBackVO;
import com.iksling.blog.vo.StatusBackVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

import static com.iksling.blog.constant.FlagConst.DELETED;

/**
 *
 */
@Service
public class FriendLinkServiceImpl extends ServiceImpl<FriendLinkMapper, FriendLink>
    implements FriendLinkService{
    @Autowired
    private FriendLinkMapper friendLinkMapper;

    @Override
    @Transactional
    public void saveOrUpdateFriendLinkBackVO(FriendLinkBackVO friendLinkBackVO) {
        Integer loginUserId = UserUtil.getLoginUser().getUserId();
        FriendLink friendLink = BeanCopyUtil.copyObject(friendLinkBackVO, FriendLink.class);
        if (friendLink.getId() == null) {
            if (friendLink.getUserId() == null || friendLink.getLinkUrl() == null || friendLink.getLinkDesc() == null || friendLink.getLinkLogo() == null || friendLink.getLinkName() == null)
                throw new OperationStatusException();
            friendLink.setCreateUser(loginUserId);
            friendLink.setCreateTime(new Date());
            friendLinkMapper.insert(friendLink);
        } else {
            friendLink.setUserId(null);
            friendLink.setUpdateUser(loginUserId);
            friendLink.setUpdateTime(new Date());
            friendLinkMapper.updateById(friendLink);
        }
    }

    @Override
    @Transactional
    public void deleteBackFriendLinksByIdList(List<Integer> idList) {
        if (idList.isEmpty())
            throw new IllegalRequestException();
        int count = friendLinkMapper.delete(new LambdaUpdateWrapper<FriendLink>()
                .eq(FriendLink::getDeletedFlag, true)
                .in(FriendLink::getId, idList));
        if (count != idList.size())
            throw new IllegalRequestException();
    }

    @Override
    @Transactional
    public void updateFriendLinksStatusBackVO(StatusBackVO statusBackVO) {
        LoginUser loginUser = UserUtil.getLoginUser();
        LambdaUpdateWrapper<FriendLink> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        if (DELETED.equals(statusBackVO.getType())) {
            if (loginUser.getRoleWeight() > 100)
                throw new IllegalRequestException();
            else
                lambdaUpdateWrapper.set(FriendLink::getDeletedFlag, false);
        } else
            lambdaUpdateWrapper.set(FriendLink::getDeletedFlag, true);
        int count = friendLinkMapper.update(null, lambdaUpdateWrapper
                .eq(loginUser.getRoleWeight() > 100, FriendLink::getDeletedFlag, false)
                .in(FriendLink::getId, statusBackVO.getIdList())
                .set(FriendLink::getUpdateUser, loginUser.getUserId())
                .set(FriendLink::getUpdateTime, new Date()));
        if (count != statusBackVO.getIdList().size())
            throw new OperationStatusException();
    }

    @Override
    public PagePojo<FriendLinksBackDTO> getFriendLinksBackDTO(ConditionBackVO condition) {
        LoginUser loginUser = UserUtil.getLoginUser();
        if (DELETED.equals(condition.getType()) && loginUser.getRoleWeight() > 100)
            return new PagePojo<>();
        Integer count = friendLinkMapper.selectFriendLinksBackDTOCount(condition);
        if (count == 0)
            return new PagePojo<>();
        condition.setCurrent((condition.getCurrent() - 1) * condition.getSize());
        List<FriendLinksBackDTO> friendLinksBackDTOList = friendLinkMapper.selectFriendLinksBackDTO(condition);
        return new PagePojo<>(count, friendLinksBackDTOList);
    }
}




