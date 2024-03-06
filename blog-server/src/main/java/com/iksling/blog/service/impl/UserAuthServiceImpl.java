package com.iksling.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iksling.blog.dto.LabelBackDTO;
import com.iksling.blog.dto.UserAuthsBackDTO;
import com.iksling.blog.entity.*;
import com.iksling.blog.exception.IllegalRequestException;
import com.iksling.blog.exception.OperationStatusException;
import com.iksling.blog.mapper.*;
import com.iksling.blog.pojo.LoginUser;
import com.iksling.blog.pojo.PagePojo;
import com.iksling.blog.service.MultiFileService;
import com.iksling.blog.service.UserAuthService;
import com.iksling.blog.service.UserConfigService;
import com.iksling.blog.service.UserRoleService;
import com.iksling.blog.util.BeanCopyUtil;
import com.iksling.blog.util.CommonUtil;
import com.iksling.blog.util.UserUtil;
import com.iksling.blog.pojo.Condition;
import com.iksling.blog.vo.PasswordVO;
import com.iksling.blog.vo.StatusBackVO;
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
import static com.iksling.blog.constant.FlagConst.DELETED;
import static com.iksling.blog.constant.FlagConst.LOCKED;
import static com.iksling.blog.enums.FileDirEnum.*;

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
    private MultiFileService multiFileService;

    @Autowired
    private SessionRegistry sessionRegistry;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void updateUserAuthBackVO(UserAuthBackVO userAuthBackVO) {
        LoginUser loginUser = UserUtil.getLoginUser();
        UserAuth userAuth = BeanCopyUtil.copyObject(userAuthBackVO, UserAuth.class);
        List<Map<String, Object>> mapList = userAuthMapper.selectMaps(new LambdaQueryWrapper<UserAuth>()
                .select(UserAuth::getUserId, UserAuth::getAssimilateFlag, UserAuth::getAssimilateNowFlag)
                .eq(UserAuth::getId, userAuth.getId())
                .eq(UserAuth::getDeletedFlag, false));
        if (mapList.isEmpty())
            throw new IllegalRequestException();
        Integer userId = (Integer) mapList.get(0).get("user_id");
        if (ROOT_USER_ID_LIST.contains(userId))
            throw new IllegalRequestException();
        if (userAuth.getPassword() != null) {
            userAuth.setPassword(passwordEncoder.encode(userAuth.getPassword()));
            userAuthBackVO.setPassword(null);
        }
        Integer loginUserId = loginUser.getUserId();
        Date createTime = new Date();
        userAuth.setUpdateUser(loginUserId);
        userAuth.setUpdateTime(createTime);
        userAuthMapper.update(userAuth, new LambdaUpdateWrapper<UserAuth>()
                .eq(UserAuth::getId, userAuth.getId()));
        if (userAuthBackVO.getRoleIdList() != null) {
            if (loginUser.getRoleWeight() > 100 && !Collections.disjoint(userAuthBackVO.getRoleIdList(), ROOT_ROLE_ID_LIST))
                throw new IllegalRequestException();
            userRoleMapper.deleteByMap(Collections.singletonMap("user_id", userId));
            userRoleService.saveBatch(userAuthBackVO.getRoleIdList().stream().map(e -> UserRole.builder()
                    .roleId(e)
                    .userId(userId)
                    .build()).collect(Collectors.toList()));
            Integer count = roleMapper.selectCount(new LambdaQueryWrapper<Role>()
                    .le(Role::getRoleWeight, 400)
                    .in(Role::getId, userAuthBackVO.getRoleIdList()));
            if ((Boolean) mapList.get(0).get("assimilate_flag")) {
                if (count == 0) {
                    userConfigMapper.update(null, new LambdaUpdateWrapper<UserConfig>()
                            .set(UserConfig::getDeletedFlag, true)
                            .eq(UserConfig::getUserId, userId));
                    userAuthMapper.update(null, new LambdaUpdateWrapper<UserAuth>()
                            .set(UserAuth::getAssimilateNowFlag, false)
                            .eq(UserAuth::getUserId, userId));
                }
            }
            else if (count > 0) {
                List<UserConfig> userConfigList = userConfigMapper.selectList(new LambdaQueryWrapper<UserConfig>()
                        .select(UserConfig::getConfigDesc, UserConfig::getConfigName, UserConfig::getConfigValue)
                        .eq(UserConfig::getUserId, ROOT_ROLE_ID));
                userConfigService.saveBatch(userConfigList.stream()
                        .peek(e -> {
                            e.setUserId(userId);
                            e.setCreateUser(loginUserId);
                            e.setCreateTime(createTime);
                        })
                        .collect(Collectors.toList()));
                userAuthMapper.update(null, new LambdaUpdateWrapper<UserAuth>()
                        .set(UserAuth::getAssimilateFlag, true)
                        .set(UserAuth::getAssimilateNowFlag, true)
                        .eq(UserAuth::getUserId, userId));
                List<Object> objectList = multiFileService.listObjs(new LambdaQueryWrapper<MultiFile>()
                        .select(MultiFile::getId)
                        .eq(MultiFile::getUserId, userId)
                        .eq(MultiFile::getFileName, IMAGE.getCurrentPath())
                        .or()
                        .eq(MultiFile::getFileName, AUDIO.getCurrentPath())
                        .orderByDesc(MultiFile::getFileName));
                List<MultiFile> multiFileList = new ArrayList<>();
                multiFileList.add(MultiFile.builder()
                        .userId(userId)
                        .parentId((Integer) objectList.get(0))
                        .fileName(IMAGE_ARTICLE.getCurrentPath())
                        .fileFullPath(userId + "/" + IMAGE_ARTICLE.getPath())
                        .fileNameOrigin(IMAGE_ARTICLE.getName())
                        .deletableFlag(false)
                        .createUser(loginUserId)
                        .createTime(createTime)
                        .build());
                multiFileList.add(MultiFile.builder()
                        .userId(userId)
                        .parentId((Integer) objectList.get(0))
                        .fileName(IMAGE_ALBUM.getCurrentPath())
                        .fileFullPath(userId + "/" + IMAGE_ALBUM.getPath())
                        .fileNameOrigin(IMAGE_ALBUM.getName())
                        .deletableFlag(false)
                        .createUser(loginUserId)
                        .createTime(createTime)
                        .build());
                multiFileList.add(MultiFile.builder()
                        .userId(userId)
                        .parentId((Integer) objectList.get(1))
                        .fileName(AUDIO_MUSIC.getCurrentPath())
                        .fileFullPath(userId + "/" + AUDIO_MUSIC.getPath())
                        .fileNameOrigin(AUDIO_MUSIC.getName())
                        .deletableFlag(false)
                        .createUser(loginUserId)
                        .createTime(createTime)
                        .build());
                multiFileService.saveBatch(multiFileList);
            }
            offlineByUserId(userId);
        }
    }

    @Override
    @Transactional
    public void updateUserAuthStatusBackVO(StatusBackVO statusBackVO) {
        LoginUser loginUser = UserUtil.getLoginUser();
        LambdaUpdateWrapper<UserAuth> lambdaUpdateWrapper = new LambdaUpdateWrapper<UserAuth>()
                .in(UserAuth::getId, statusBackVO.getIdList())
                .eq(UserAuth::getDeletedFlag, false)
                .notIn(loginUser.getRoleWeight() > 100, UserAuth::getUserId, ROOT_USER_ID_LIST);
        if (LOCKED.equals(statusBackVO.getType()))
            lambdaUpdateWrapper.setSql("locked_flag = !locked_flag");
        else
            lambdaUpdateWrapper.setSql("disabled_flag = !disabled_flag");
        int count = userAuthMapper.update(null, lambdaUpdateWrapper);
        if (count != statusBackVO.getIdList().size())
            throw new OperationStatusException();
    }

    @Override
    @Transactional
    public void updateUserPasswordVO(PasswordVO passwordVO) {
        Integer loginUserId = UserUtil.getLoginUser().getUserId();
        List<Object> objectList = userAuthMapper.selectObjs(new LambdaQueryWrapper<UserAuth>()
                .select(UserAuth::getPassword)
                .eq(UserAuth::getUserId, loginUserId));
        if (passwordEncoder.matches(passwordVO.getOldPassword(), objectList.get(0).toString())) {
            userAuthMapper.update(null, new LambdaUpdateWrapper<UserAuth>()
                    .set(UserAuth::getPassword, passwordEncoder.encode(passwordVO.getNewPassword()))
                    .set(UserAuth::getUpdateUser, loginUserId)
                    .set(UserAuth::getUpdateTime, new Date()));
            passwordVO.setOldPassword(null);
            passwordVO.setNewPassword(null);
        } else {
            passwordVO.setOldPassword(null);
            passwordVO.setNewPassword(null);
            throw new OperationStatusException("密码错误!");
        }
    }

    @Override
    public PagePojo<UserAuthsBackDTO> getUserAuthsBackDTO(Condition condition) {
        if (DELETED.equals(condition.getType()) && UserUtil.getLoginUser().getRoleWeight() > 100)
            return new PagePojo<>();
        Integer count = userAuthMapper.selectUserAuthsBackDTOCount(condition);
        if (count == 0)
            return new PagePojo<>();
        condition.setCurrent((condition.getCurrent() - 1) * condition.getSize());
        List<UserAuthsBackDTO> userAuthsBackDTOList = userAuthMapper.selectUserAuthsBackDTO(condition);
        return new PagePojo<>(count, userAuthsBackDTOList);
    }

    @Override
    public List<LabelBackDTO> getBackUsernames(String keywords) {
        List<Map<String, Object>> mapList = userAuthMapper.selectMaps(new LambdaQueryWrapper<UserAuth>()
                .select(UserAuth::getUserId, UserAuth::getUsername)
                .eq(UserUtil.getLoginUser().getRoleWeight() > 100, UserAuth::getDeletedFlag, false)
                .likeRight(CommonUtil.isNotEmpty(keywords), UserAuth::getUsername, keywords));
        return mapList.stream().map(e -> LabelBackDTO.builder()
                        .id((Integer)e.get("user_id"))
                        .label(e.get("username").toString())
                        .build()).collect(Collectors.toList());
    }

    private void offlineByUserId(Integer userId) {
        List<Object> loginUserList = sessionRegistry.getAllPrincipals().stream().filter(e -> {
            LoginUser loginUser = (LoginUser) e;
            return loginUser.getUserId().equals(userId);
        }).collect(Collectors.toList());
        List<SessionInformation> allSessions = new ArrayList<>();
        loginUserList.forEach(e -> allSessions.addAll(sessionRegistry.getAllSessions(e, false)));
        allSessions.forEach(SessionInformation::expireNow);
    }
}




