package com.iksling.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iksling.blog.dto.LabelDTO;
import com.iksling.blog.entity.UserAuth;
import com.iksling.blog.exception.IllegalRequestException;
import com.iksling.blog.mapper.UserAuthMapper;
import com.iksling.blog.pojo.LoginUser;
import com.iksling.blog.service.UserAuthService;
import com.iksling.blog.util.UserUtil;
import com.iksling.blog.vo.CommonStatusVO;
import com.iksling.blog.vo.UpdateBatchVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 *
 */
@Service
public class UserAuthServiceImpl extends ServiceImpl<UserAuthMapper, UserAuth>
    implements UserAuthService{
    @Autowired
    private UserAuthMapper userAuthMapper;

    @Override
    public List<LabelDTO> getAllUsername(String keywords) {
        List<UserAuth> userAuthList = userAuthMapper.selectList(new LambdaQueryWrapper<UserAuth>()
                .select(UserAuth::getId, UserAuth::getUserId, UserAuth::getUsername)
                .likeRight(StringUtils.isNotBlank(keywords), UserAuth::getUsername, keywords.trim()));
        return userAuthList.stream()
                .map(e -> LabelDTO.builder()
                        .id(e.getId())
                        .userId(e.getUserId())
                        .label(e.getUsername())
                        .build())
                .collect(Collectors.toList());
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
    }

    @Override
    @Transactional
    public void updateUserAuthSStatus(UpdateBatchVO updateBatchVO) {
        if (UserUtil.getLoginUser().getRoleWeight() > 100 && !updateBatchVO.getDeletedFlag())
            throw new IllegalRequestException();
        Integer count = userAuthMapper.updateUserAuthSStatus(updateBatchVO);
        if (count != updateBatchVO.getIdList().size())
            throw new IllegalRequestException();
    }
}




