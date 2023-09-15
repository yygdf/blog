package com.iksling.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iksling.blog.dto.LabelDTO;
import com.iksling.blog.dto.UserAuthsBackDTO;
import com.iksling.blog.entity.Role;
import com.iksling.blog.entity.UserAuth;
import com.iksling.blog.entity.UserConfig;
import com.iksling.blog.entity.UserRole;
import com.iksling.blog.exception.IllegalRequestException;
import com.iksling.blog.mapper.RoleMapper;
import com.iksling.blog.mapper.UserAuthMapper;
import com.iksling.blog.mapper.UserConfigMapper;
import com.iksling.blog.mapper.UserRoleMapper;
import com.iksling.blog.pojo.LoginUser;
import com.iksling.blog.pojo.PagePojo;
import com.iksling.blog.service.UserAuthService;
import com.iksling.blog.service.UserConfigService;
import com.iksling.blog.service.UserRoleService;
import com.iksling.blog.util.UserUtil;
import com.iksling.blog.vo.CommonStatusVO;
import com.iksling.blog.vo.ConditionVO;
import com.iksling.blog.vo.UpdateBatchVO;
import com.iksling.blog.vo.UserAuthBackVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static com.iksling.blog.constant.CommonConst.*;

/**
 *
 */
@Service
public class UserAuthServiceImpl extends ServiceImpl<UserAuthMapper, UserAuth>
    implements UserAuthService{
    @Autowired
    private UserAuthMapper userAuthMapper;

    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private UserRoleMapper userRoleMapper;
    @Autowired
    private UserConfigMapper userConfigMapper;

    @Autowired
    private UserRoleService userRoleService;
    @Autowired
    private UserConfigService userConfigService;

    @Autowired
    private SessionRegistry sessionRegistry;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public List<LabelDTO> getBackUsernames(String keywords) {
        if (StringUtils.isNotBlank(keywords))
            keywords = keywords.trim();
        List<UserAuth> userAuthList = userAuthMapper.selectList(new LambdaQueryWrapper<UserAuth>()
                .select(UserAuth::getId, UserAuth::getUserId, UserAuth::getUsername)
                .eq(UserUtil.getLoginUser().getRoleWeight() > 100, UserAuth::getDeletedFlag, false)
                .likeRight(StringUtils.isNotBlank(keywords), UserAuth::getUsername, keywords));
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
        Integer count = userAuthMapper.selectUserAuthsBackDTOCount(condition);
        if (count == 0)
            return new PagePojo<>();
        condition.setCurrent((condition.getCurrent() - 1) * condition.getSize());
        List<UserAuthsBackDTO> userAuthsBackDTOList = userAuthMapper.listUserAuthsBackDTO(condition);
        return new PagePojo<>(count, userAuthsBackDTOList);
    }

    @Override
    public boolean getBackUserAuthExistFlag(String keywords) {
        if (StringUtils.isBlank(keywords))
            return false;
        return userAuthMapper.selectCount(new LambdaQueryWrapper<UserAuth>()
                .eq(UserAuth::getUsername, keywords.trim())) != 0;
    }

    @Override
    @Transactional
    public void updateUserAuthBackVO(UserAuthBackVO userAuthBackVO) {
        LoginUser loginUser = UserUtil.getLoginUser();
        // TODO: 这里直接用核心用户idList替代核心角色idList进行卡控，需保持核心用户都是核心角色
        if (loginUser.getRoleWeight() > 100 && (userAuthBackVO.getLockedFlag() || ROOT_USER_ID_LIST.contains(userAuthBackVO.getId()) || !Collections.disjoint(ROOT_ROLE_ID_LIST, userAuthBackVO.getRoleIdList())))
            throw new IllegalRequestException();
        if (StringUtils.isNotBlank(userAuthBackVO.getPassword()))
            userAuthBackVO.setPassword(passwordEncoder.encode(userAuthBackVO.getPassword().trim()));
        int count = userAuthMapper.update(null, new LambdaUpdateWrapper<UserAuth>()
                .set(StringUtils.isNotBlank(userAuthBackVO.getPassword()), UserAuth::getPassword, userAuthBackVO.getPassword())
                .set(UserAuth::getLockedFlag, userAuthBackVO.getLockedFlag())
                .set(UserAuth::getDisabledFlag, userAuthBackVO.getDisabledFlag())
                .set(UserAuth::getUpdateUser, loginUser.getUserId())
                .set(UserAuth::getUpdateTime, new Date())
                .eq(UserAuth::getUserId, userAuthBackVO.getId())
                .eq(loginUser.getRoleWeight() > 100, UserAuth::getDeletedFlag, false));
        if (count != 1)
            throw new IllegalRequestException();
        userRoleMapper.deleteByMap(Collections.singletonMap("user_id", userAuthBackVO.getId()));
        List<UserRole> userRoleList = userAuthBackVO.getRoleIdList().stream().map(roleId -> UserRole.builder()
                .roleId(roleId)
                .userId(userAuthBackVO.getId())
                .build()).collect(Collectors.toList());
        userRoleService.saveBatch(userRoleList);
        Integer userConfigFlag = userAuthMapper.selectCount(new LambdaQueryWrapper<UserAuth>()
                .eq(UserAuth::getUserId, userAuthBackVO.getId())
                .eq(UserAuth::getUserConfigFlag, true));
        Integer countNew = roleMapper.selectCount(new LambdaQueryWrapper<Role>()
                .le(Role::getRoleWeight, 400)
                .in(Role::getId, userAuthBackVO.getRoleIdList()));
        if (userConfigFlag > 0) {
            if (countNew == 0)
                userConfigMapper.update(null, new LambdaUpdateWrapper<UserConfig>()
                        .set(UserConfig::getDeletedFlag, true)
                        .eq(UserConfig::getUserId, userAuthBackVO.getId()));
        }
        else if (countNew > 0) {
                List<UserConfig> userConfigList = userConfigMapper.selectList(new LambdaQueryWrapper<UserConfig>()
                        .select(UserConfig::getConfigDesc, UserConfig::getConfigName, UserConfig::getConfigValue)
                        .eq(UserConfig::getUserId, ROOT_USER_ID));
                userConfigService.saveBatch(userConfigList.stream()
                        .peek(item -> {
                            item.setUserId(userAuthBackVO.getId());
                            item.setCreateUser(loginUser.getUserId());
                            item.setCreateTime(new Date());
                        })
                        .collect(Collectors.toList()));
                userAuthMapper.update(null, new LambdaUpdateWrapper<UserAuth>()
                        .set(UserAuth::getUserConfigFlag, true)
                        .eq(UserAuth::getUserId, userAuthBackVO.getId()));
            }
        deleteUserIdList(Collections.singletonList(userAuthBackVO.getId()));
    }

    @Override
    @Transactional
    public void updateUserAuthStatusVO(CommonStatusVO commonStatusVO) {
        LoginUser loginUser = UserUtil.getLoginUser();
        // TODO: 这里直接用核心用户idList替代核心角色idList进行卡控，需保持核心用户都是核心角色
        if (loginUser.getRoleWeight() > 100 && (Objects.nonNull(commonStatusVO.getTopFlag()) && commonStatusVO.getTopFlag()) || ROOT_USER_ID_LIST.contains(commonStatusVO.getId()))
                throw new IllegalRequestException();
        int count = userAuthMapper.update(null, new LambdaUpdateWrapper<UserAuth>()
                .set(Objects.nonNull(commonStatusVO.getTopFlag()), UserAuth::getLockedFlag, commonStatusVO.getTopFlag())
                .set(UserAuth::getDisabledFlag, commonStatusVO.getPublicFlag())
                .eq(UserAuth::getUserId, commonStatusVO.getId())
                .eq(loginUser.getRoleWeight() > 100, UserAuth::getDeletedFlag, false));
        if (count != 1)
            throw new IllegalRequestException();
        if (commonStatusVO.getPublicFlag() || (Objects.nonNull(commonStatusVO.getTopFlag()) && commonStatusVO.getTopFlag()))
            deleteUserIdList(Collections.singletonList(commonStatusVO.getId()));
    }

    @Override
    @Transactional
    public void updateUserAuthsStatus(UpdateBatchVO updateBatchVO) {
        if (updateBatchVO.getIdList().contains(ROOT_USER_ID) || (UserUtil.getLoginUser().getRoleWeight() > 100 && (!Collections.disjoint(updateBatchVO.getIdList(), ROOT_USER_ID_LIST) || !updateBatchVO.getDeletedFlag())))
            throw new IllegalStateException();
        Integer count = userAuthMapper.updateUserAuthsStatus(updateBatchVO);
        if (count != updateBatchVO.getIdList().size())
            throw new IllegalRequestException();
        if (updateBatchVO.getDeletedFlag())
            deleteUserIdList(updateBatchVO.getIdList());
    }

    private void deleteUserIdList(List<Integer> idList) {
        List<Object> loginUserList = sessionRegistry.getAllPrincipals().stream().filter(item -> {
            LoginUser loginUser = (LoginUser) item;
            return idList.contains(loginUser.getUserId());
        }).collect(Collectors.toList());
        List<SessionInformation> allSessions = new ArrayList<>();
        loginUserList.forEach(item -> allSessions.addAll(sessionRegistry.getAllSessions(item, false)));
        allSessions.forEach(SessionInformation::expireNow);
    }
}




