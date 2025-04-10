package com.iksling.blog.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iksling.blog.dto.LabelBackDTO;
import com.iksling.blog.dto.UserAuthsBackDTO;
import com.iksling.blog.entity.*;
import com.iksling.blog.exception.IllegalRequestException;
import com.iksling.blog.exception.OperationStatusException;
import com.iksling.blog.mapper.*;
import com.iksling.blog.pojo.Condition;
import com.iksling.blog.pojo.LoginUser;
import com.iksling.blog.pojo.PagePojo;
import com.iksling.blog.service.MultiFileService;
import com.iksling.blog.service.UserAuthService;
import com.iksling.blog.service.UserConfigService;
import com.iksling.blog.service.UserRoleService;
import com.iksling.blog.util.*;
import com.iksling.blog.vo.PasswordForgetVO;
import com.iksling.blog.vo.PasswordVO;
import com.iksling.blog.vo.StatusBackVO;
import com.iksling.blog.vo.UserAuthBackVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.iksling.blog.constant.CommonConst.*;
import static com.iksling.blog.constant.FlagConst.DELETED;
import static com.iksling.blog.constant.FlagConst.LOCKED;
import static com.iksling.blog.constant.RedisConst.EMAIL_FORGET_CODE;
import static com.iksling.blog.constant.RedisConst.LOGIN_TOKEN;
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
    private UserMapper userMapper;
    @Autowired
    private UserRoleMapper userRoleMapper;
    @Autowired
    private UserConfigMapper userConfigMapper;
    @Autowired
    private QQAuthMapper qqAuthMapper;
    @Autowired
    private NoticeMapper noticeMapper;

    @Autowired
    private UserRoleService userRoleService;
    @Autowired
    private UserConfigService userConfigService;
    @Autowired
    private MultiFileService multiFileService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void updateUserAuthBackVO(UserAuthBackVO userAuthBackVO) {
        LoginUser loginUser = UserUtil.getLoginUser();
        UserAuth userAuth = BeanCopyUtil.copyObject(userAuthBackVO, UserAuth.class);
        UserAuth userAuthOrigin = userAuthMapper.selectOne(new LambdaQueryWrapper<UserAuth>()
                .select(UserAuth::getUserId, UserAuth::getAssimilateFlag)
                .eq(UserAuth::getId, userAuth.getId())
                .eq(UserAuth::getDeletedFlag, false));
        if (userAuthOrigin == null)
            throw new IllegalRequestException();
        Integer userId = userAuthOrigin.getUserId();
        if (ROOT_USER_ID_LIST.contains(userId) && !loginUser.getUserId().equals(ROOT_USER_ID))
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
        boolean modifyRoleFlag = false;
        if (userAuthBackVO.getRoleIdList() != null) {
            modifyRoleFlag = true;
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
            if (userAuthOrigin.getAssimilateFlag()) {
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
                        .select(UserConfig::getConfigDesc, UserConfig::getConfigName, UserConfig::getConfigValue, UserConfig::getDeletedFlag)
                        .eq(UserConfig::getUserId, ROOT_USER_ID));
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
                        .and(e -> e.eq(MultiFile::getFileName, IMAGE.getCurrentPath())
                                .or()
                                .eq(MultiFile::getFileName, AUDIO.getCurrentPath()))
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
                objectList = userMapper.selectObjs(new LambdaQueryWrapper<User>()
                        .select(User::getEmail)
                        .eq(User::getId, userId));
                String email = objectList.get(0).toString();
                if (!email.equals("")) {
                    noticeMapper.insert(Notice.builder()
                            .userId(userId)
                            .noticeType(4)
                            .noticeTypeSub(2)
                            .noticeTitle(LocaleUtil.getMessage("S0033"))
                            .noticeContent(LocaleUtil.getMessage("S0034", WEBSITE_URL_BACK, WEBSITE_URL, userId))
                            .createUser(userId)
                            .createTime(createTime).build());
                    RabbitUtil.sendEmail(email, LocaleUtil.getMessage("S0033"), LocaleUtil.getMessage("S0034", WEBSITE_URL_BACK, WEBSITE_URL, userId));
                }
            }
            offlineByUserId(userId);
        }
        LambdaUpdateWrapper<QQAuth> lambdaUpdateWrapper = new LambdaUpdateWrapper<QQAuth>()
                .eq(QQAuth::getUserId, userId);
        boolean flag = false;
        boolean modifyStatusFlag = false;
        if (userAuth.getLockedFlag() != null) {
            lambdaUpdateWrapper.set(QQAuth::getLockedFlag, userAuth.getLockedFlag());
            flag = true;
            if (userAuth.getLockedFlag() == Boolean.TRUE)
                modifyStatusFlag = true;
        }
        if (userAuth.getDisabledFlag() != null) {
            lambdaUpdateWrapper.set(QQAuth::getDisabledFlag, userAuth.getDisabledFlag());
            flag = true;
            if (userAuth.getDisabledFlag() == Boolean.TRUE)
                modifyStatusFlag = true;
        }
        if (flag)
            qqAuthMapper.update(null, lambdaUpdateWrapper);
        if (!modifyRoleFlag && modifyStatusFlag)
            offlineByUserId(userId);
    }

    @Override
    @Transactional
    public void updateUserAuthStatusBackVO(StatusBackVO statusBackVO) {
        LoginUser loginUser = UserUtil.getLoginUser();
        LambdaUpdateWrapper<UserAuth> lambdaUpdateWrapper = new LambdaUpdateWrapper<UserAuth>()
                .in(UserAuth::getId, statusBackVO.getIdList())
                .eq(UserAuth::getDeletedFlag, false)
                .notIn(loginUser.getRoleWeight() > 100, UserAuth::getUserId, ROOT_USER_ID_LIST);
        List<Object> userIdList = userAuthMapper.selectObjs(new LambdaQueryWrapper<UserAuth>()
                .select(UserAuth::getUserId)
                .in(UserAuth::getId, statusBackVO.getIdList()));
        LambdaUpdateWrapper<QQAuth> lambdaUpdateWrapper2 = new LambdaUpdateWrapper<QQAuth>()
                .in(QQAuth::getUserId, userIdList);
        boolean flag = false;
        if (LOCKED.equals(statusBackVO.getType())) {
            if (statusBackVO.getStatus() == Boolean.TRUE) {
                lambdaUpdateWrapper.set(UserAuth::getLockedFlag, false);
                lambdaUpdateWrapper2.set(QQAuth::getLockedFlag, false);
            } else {
                lambdaUpdateWrapper.set(UserAuth::getLockedFlag, true);
                lambdaUpdateWrapper2.set(QQAuth::getLockedFlag, true);
                flag = true;
            }
        } else {
            if (statusBackVO.getStatus() == Boolean.TRUE) {
                lambdaUpdateWrapper.set(UserAuth::getDisabledFlag, false);
                lambdaUpdateWrapper2.set(QQAuth::getDisabledFlag, false);
            } else {
                lambdaUpdateWrapper.set(UserAuth::getDisabledFlag, true);
                lambdaUpdateWrapper2.set(QQAuth::getDisabledFlag, true);
                flag = true;
            }
        }
        int count = userAuthMapper.update(null, lambdaUpdateWrapper);
        if (count != statusBackVO.getIdList().size())
            throw new OperationStatusException();
        qqAuthMapper.update(null, lambdaUpdateWrapper2);
        if (flag)
            offlineByUserId(userIdList.stream().map(e -> (Integer) e).collect(Collectors.toList()));
    }

    @Override
    @Transactional
    public void updateBackUsername(String username) {
        Object o = JSON.parseObject(username, Map.class).get("username");
        if (o == null)
            throw new OperationStatusException();
        String usernameVal = o.toString().trim();
        if (usernameVal.length() < 1 || usernameVal.length() > 50)
            throw new OperationStatusException(LocaleUtil.getMessage("S0018"));
        Integer count = userAuthMapper.selectCount(new LambdaQueryWrapper<UserAuth>()
                .eq(UserAuth::getUsername, usernameVal)
                .eq(UserAuth::getDeletedFlag, false));
        if (count != 0)
            throw new OperationStatusException(LocaleUtil.getMessage("S0019"));
        Integer loginUserId = UserUtil.getLoginUser().getUserId();
        count = userMapper.update(null, new LambdaUpdateWrapper<User>()
                .set(User::getModifiedFlag, true)
                .eq(User::getId, loginUserId)
                .eq(User::getModifiedFlag, false));
        if (count == 0)
            throw new OperationStatusException();
        userAuthMapper.update(null, new LambdaUpdateWrapper<UserAuth>()
                .set(UserAuth::getUsername, usernameVal)
                .set(UserAuth::getUpdateUser, loginUserId)
                .set(UserAuth::getUpdateTime, new Date())
                .eq(UserAuth::getUserId, loginUserId));
    }

    @Override
    @Transactional
    public void updateUserPasswordVO(PasswordVO passwordVO) {
        Integer loginUserId = UserUtil.getLoginUser().getUserId();
        if (DISABLE_MOD_PASS_USER_ID_LIST.contains(loginUserId))
            return;
        UserAuth userAuth = userAuthMapper.selectOne(new LambdaQueryWrapper<UserAuth>()
                .select(UserAuth::getPassword, UserAuth::getDisabledFlag)
                .eq(UserAuth::getUserId, loginUserId));
        if (passwordEncoder.matches(passwordVO.getOldPassword(), userAuth.getPassword())) {
            userAuthMapper.update(null, new LambdaUpdateWrapper<UserAuth>()
                    .set(UserAuth::getPassword, passwordEncoder.encode(passwordVO.getNewPassword()))
                    .set(userAuth.getDisabledFlag(), UserAuth::getDisabledFlag, false)
                    .set(UserAuth::getUpdateUser, loginUserId)
                    .set(UserAuth::getUpdateTime, new Date())
                    .eq(UserAuth::getUserId, loginUserId));
            if (userAuth.getDisabledFlag())
                qqAuthMapper.update(null, new LambdaUpdateWrapper<QQAuth>()
                        .set(QQAuth::getDisabledFlag, false)
                        .eq(QQAuth::getUserId, loginUserId));
            passwordVO.setOldPassword(null);
            passwordVO.setNewPassword(null);
        } else {
            passwordVO.setOldPassword(null);
            passwordVO.setNewPassword(null);
            throw new OperationStatusException(LocaleUtil.getMessage("S0020"));
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
        List<UserAuth> userAuthList = userAuthMapper.selectList(new LambdaQueryWrapper<UserAuth>()
                .select(UserAuth::getUserId, UserAuth::getUsername)
                .eq(UserUtil.getLoginUser().getRoleWeight() > 100, UserAuth::getDeletedFlag, false)
                .likeRight(CommonUtil.isNotEmpty(keywords), UserAuth::getUsername, keywords));
        return userAuthList.stream().map(e -> LabelBackDTO.builder()
                        .id(e.getUserId())
                        .label(e.getUsername())
                        .build()).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void updateUserForgetPasswordVO(PasswordForgetVO passwordForgetVO) {
        Integer loginUserId = UserUtil.getLoginUser().getUserId();
        if (DISABLE_MOD_PASS_USER_ID_LIST.contains(loginUserId))
            return;
        String email = passwordForgetVO.getEmail();
        if (!RegexUtil.checkEmail(email))
            throw new OperationStatusException();
        List<Object> objectList = userMapper.selectObjs(new LambdaQueryWrapper<User>()
                .select(User::getId)
                .eq(User::getEmail, email)
                .exists("select id from tb_user_auth where user_id=id and deleted_flag=false"));
        if (objectList.isEmpty())
            throw new OperationStatusException(LocaleUtil.getMessage("S0021"));
        String code = RedisUtil.getValue(EMAIL_FORGET_CODE + "_" + email);
        if (code == null)
            throw new OperationStatusException(LocaleUtil.getMessage("S0022"));
        if (!passwordForgetVO.getCode().equals(code))
            throw new OperationStatusException(LocaleUtil.getMessage("S0023"));
        userAuthMapper.update(null, new LambdaUpdateWrapper<UserAuth>()
                        .set(UserAuth::getPassword, passwordEncoder.encode(passwordForgetVO.getPassword()))
                        .set(UserAuth::getUpdateUser, objectList.get(0))
                        .set(UserAuth::getUpdateTime, new Date())
                        .eq(UserAuth::getUserId, objectList.get(0)));
        RedisUtil.expire(EMAIL_FORGET_CODE + "_" + email, 0, TimeUnit.MILLISECONDS);
    }

    private void offlineByUserId(Integer userId) {
        RedisUtil.delKey(LOGIN_TOKEN + "_" + userId);
    }

    private void offlineByUserId(List<Integer> idList) {
        RedisUtil.delKey(idList.stream().map(e -> LOGIN_TOKEN + "_" + e).collect(Collectors.toList()));
    }
}




