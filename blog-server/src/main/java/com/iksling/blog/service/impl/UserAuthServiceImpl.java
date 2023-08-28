package com.iksling.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iksling.blog.dto.LabelDTO;
import com.iksling.blog.dto.UserAuthsBackDTO;
import com.iksling.blog.entity.UserAuth;
import com.iksling.blog.exception.IllegalRequestException;
import com.iksling.blog.mapper.UserAuthMapper;
import com.iksling.blog.pojo.LoginUser;
import com.iksling.blog.pojo.PagePojo;
import com.iksling.blog.service.UserAuthService;
import com.iksling.blog.util.UserUtil;
import com.iksling.blog.vo.CommonStatusVO;
import com.iksling.blog.vo.ConditionVO;
import com.iksling.blog.vo.UpdateBatchVO;
import com.iksling.blog.vo.UserAuthBackVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.iksling.blog.constant.CommonConst.ROOT_USER_ID;

/**
 *
 */
@Service
public class UserAuthServiceImpl extends ServiceImpl<UserAuthMapper, UserAuth>
    implements UserAuthService{
    @Autowired
    private UserAuthMapper userAuthMapper;

    @Autowired
    private SessionRegistry sessionRegistry;

    @Override
    public List<LabelDTO> getBackUsernames(String keywords) {
        List<UserAuth> userAuthList = userAuthMapper.selectList(new LambdaQueryWrapper<UserAuth>()
                .select(UserAuth::getId, UserAuth::getUserId, UserAuth::getUsername)
                .likeRight(StringUtils.isNotBlank(keywords.trim()), UserAuth::getUsername, keywords.trim()));
        return userAuthList.stream()
                .map(e -> LabelDTO.builder()
                        .id(e.getId())
                        .userId(e.getUserId())
                        .label(e.getUsername())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public PagePojo<UserAuthsBackDTO> getPageUserAuthsBackDTO(ConditionVO condition) {
        if (UserUtil.getLoginUser().getRoleWeight() > 100 && Objects.equals(condition.getDeletedFlag(), true))
            throw new IllegalRequestException();
        condition.setCurrent((condition.getCurrent() - 1) * condition.getSize());
        List<UserAuthsBackDTO> userAuthsBackDTOList = userAuthMapper.listUserAuthsBackDTO(condition);
        if (CollectionUtils.isEmpty(userAuthsBackDTOList))
            return new PagePojo<>();
        return new PagePojo<>(userAuthsBackDTOList.size(), userAuthsBackDTOList);
    }

    @Override
    public boolean getBackUserAuthExistFlag(String keywords) {
        if (StringUtils.isBlank(keywords.trim()))
            return false;
        return userAuthMapper.selectCount(new LambdaQueryWrapper<UserAuth>()
                .eq(UserAuth::getUsername, keywords.trim())) != 0;
    }

    @Override
    @Transactional
    public void updateUserAuthBackVO(UserAuthBackVO userAuthBackVO) {

    }

    @Override
    @Transactional
    public void updateUserAuthStatusVO(CommonStatusVO commonStatusVO) {
        LoginUser loginUser = UserUtil.getLoginUser();
        if (loginUser.getRoleWeight() > 100 && Objects.nonNull(commonStatusVO.getTopFlag()) && commonStatusVO.getTopFlag())
            throw new IllegalRequestException();
        int count = userAuthMapper.update(null, new LambdaUpdateWrapper<UserAuth>()
                .set(Objects.nonNull(commonStatusVO.getTopFlag()), UserAuth::getLockedFlag, commonStatusVO.getTopFlag())
                .set(UserAuth::getDisabledFlag, commonStatusVO.getPublicFlag())
                .eq(UserAuth::getUserId, commonStatusVO.getId())
                .eq(loginUser.getRoleWeight() > 100, UserAuth::getDeletedFlag, false));
        if (count != 1)
            throw new IllegalRequestException();
        if (commonStatusVO.getPublicFlag() || (Objects.nonNull(commonStatusVO.getTopFlag()) && commonStatusVO.getTopFlag())) {
            List<Integer> idList = new ArrayList<>();
            idList.add(commonStatusVO.getId());
            disabledOrLockedOrDeletedUserAuth(idList);
        }
    }

    @Override
    @Transactional
    public void updateUserAuthsStatus(UpdateBatchVO updateBatchVO) {
        if ((UserUtil.getLoginUser().getRoleWeight() > 100 && !updateBatchVO.getDeletedFlag()) || !Collections.disjoint(ROOT_USER_ID, updateBatchVO.getIdList()))
            throw new IllegalRequestException();
        Integer count = userAuthMapper.updateUserAuthsStatus(updateBatchVO);
        if (count != updateBatchVO.getIdList().size())
            throw new IllegalRequestException();
        if (updateBatchVO.getDeletedFlag())
            disabledOrLockedOrDeletedUserAuth(updateBatchVO.getIdList());
    }

    private void disabledOrLockedOrDeletedUserAuth(List<Integer> idList) {
        List<Object> loginUserList = sessionRegistry.getAllPrincipals().stream().filter(item -> {
            LoginUser loginUser = (LoginUser) item;
            return idList.contains(loginUser.getId());
        }).collect(Collectors.toList());
        List<SessionInformation> allSessions = new ArrayList<>();
        loginUserList.forEach(item -> allSessions.addAll(sessionRegistry.getAllSessions(item, false)));
        allSessions.forEach(SessionInformation::expireNow);
    }
}




