package com.iksling.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
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
    @Transactional
    public void saveOrUpdateFriendLinkBackVO(FriendLinkBackVO friendLinkBackVO) {
        LoginUser loginUser = UserUtil.getLoginUser();
        FriendLink friendLink = BeanCopyUtil.copyObject(friendLinkBackVO, FriendLink.class);
        if (Objects.isNull(friendLink.getId())) {
            if (StringUtils.isBlank(friendLink.getLinkUrl()) || StringUtils.isBlank(friendLink.getLinkDesc()) || StringUtils.isBlank(friendLink.getLinkLogo()) || StringUtils.isBlank(friendLink.getLinkName()))
                throw new OperationStatusException();
            friendLink.setUserId(loginUser.getUserId());
            friendLink.setCreateUser(loginUser.getUserId());
            friendLink.setCreateTime(new Date());
            friendLinkMapper.insert(friendLink);
        } else {
            if (Objects.nonNull(friendLink.getLinkUrl())) {
                if (StringUtils.isBlank(friendLink.getLinkUrl()))
                    throw new OperationStatusException();
            }
            if (Objects.nonNull(friendLink.getLinkDesc())) {
                if (StringUtils.isBlank(friendLink.getLinkDesc()))
                    throw new OperationStatusException();
            }
            if (Objects.nonNull(friendLink.getLinkLogo())) {
                if (StringUtils.isBlank(friendLink.getLinkLogo()))
                    throw new OperationStatusException();
            }
            if (Objects.nonNull(friendLink.getLinkName())) {
                if (StringUtils.isBlank(friendLink.getLinkName()))
                    throw new OperationStatusException();
            }
            friendLink.setUserId(null);
            friendLink.setUpdateUser(loginUser.getUserId());
            friendLink.setUpdateTime(new Date());
            friendLinkMapper.updateById(friendLink);
        }
    }

    @Override
    @Transactional
    public void deleteFriendLinksByIdList(List<Integer> idList) {
        if (CollectionUtils.isEmpty(idList))
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
        LambdaUpdateWrapper<FriendLink> lambdaUpdateWrapper = new LambdaUpdateWrapper<FriendLink>()
                .eq(loginUser.getRoleWeight() > 100, FriendLink::getDeletedFlag, false)
                .in(FriendLink::getId, statusBackVO.getIdList());
        if (Objects.equals(statusBackVO.getType(), 7)) {
            if (loginUser.getRoleWeight() > 100)
                throw new IllegalRequestException();
            else
                lambdaUpdateWrapper.set(FriendLink::getDeletedFlag, false);
        } else
            lambdaUpdateWrapper.set(FriendLink::getDeletedFlag, true);
        int count = friendLinkMapper.update(null, lambdaUpdateWrapper);
        if (count != statusBackVO.getIdList().size())
            throw new OperationStatusException();
    }

    @Override
    public PagePojo<FriendLinksBackDTO> getFriendLinksBackDTO(ConditionBackVO condition) {
        LoginUser loginUser = UserUtil.getLoginUser();
        if (Objects.equals(condition.getType(), 7) && loginUser.getRoleWeight() > 100)
            return new PagePojo<>();
        Integer count = friendLinkMapper.selectFriendLinksBackDTOCount(condition);
        if (count == 0)
            return new PagePojo<>();
        condition.setCurrent((condition.getCurrent() - 1) * condition.getSize());
        List<FriendLinksBackDTO> friendLinksBackDTOList = friendLinkMapper.selectFriendLinksBackDTO(condition);
        return new PagePojo<>(count, friendLinksBackDTOList);
    }
}




