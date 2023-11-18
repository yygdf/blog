package com.iksling.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iksling.blog.dto.UserOnlinesBackDTO;
import com.iksling.blog.dto.UsersBackDTO;
import com.iksling.blog.entity.MultiFile;
import com.iksling.blog.entity.User;
import com.iksling.blog.entity.UserAuth;
import com.iksling.blog.entity.UserRole;
import com.iksling.blog.exception.IllegalRequestException;
import com.iksling.blog.exception.OperationStatusException;
import com.iksling.blog.mapper.UserAuthMapper;
import com.iksling.blog.mapper.UserMapper;
import com.iksling.blog.mapper.UserRoleMapper;
import com.iksling.blog.pojo.LoginUser;
import com.iksling.blog.pojo.PagePojo;
import com.iksling.blog.service.MultiFileService;
import com.iksling.blog.service.UserService;
import com.iksling.blog.util.BeanCopyUtil;
import com.iksling.blog.util.CommonUtil;
import com.iksling.blog.util.RegexUtil;
import com.iksling.blog.util.UserUtil;
import com.iksling.blog.vo.ConditionBackVO;
import com.iksling.blog.vo.StatusBackVO;
import com.iksling.blog.vo.UserBackVO;
import com.iksling.blog.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static com.iksling.blog.constant.CommonConst.*;
import static com.iksling.blog.constant.FlagConst.DELETED;
import static com.iksling.blog.enums.FileEnum.*;

/**
 *
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserAuthMapper userAuthMapper;
    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    private MultiFileService multiFileService;

    @Autowired
    private SessionRegistry sessionRegistry;

    @Override
    @Transactional
    public void saveOrUpdateUserBackVO(UserBackVO userBackVO) {
        LoginUser loginUser = UserUtil.getLoginUser();
        Integer loginUserId = loginUser.getUserId();
        User user = BeanCopyUtil.copyObject(userBackVO, User.class);
        Integer userId = user.getId();
        if (userId == null) {
            if (userBackVO.getUsername() == null || user.getNickname() == null || !RegexUtil.checkEmail(user.getEmail()))
                throw new OperationStatusException();
            if (userMapper.selectBackUserAvatarById(user.getEmail(), userBackVO.getUsername(), null) != null)
                throw new OperationStatusException("用户已存在!");
            Date createTime = new Date();
            user.setAvatar(null);
            user.setCreateUser(loginUserId);
            user.setCreateTime(createTime);
            userMapper.insert(user);
            userAuthMapper.insert(UserAuth.builder()
                    .userId(userId)
                    .username(userBackVO.getUsername())
                    .password(DEFAULT_PASSWORD)
                    .createUser(loginUserId)
                    .createTime(createTime)
                    .build());
            userRoleMapper.insert(UserRole.builder()
                    .userId(userId)
                    .roleId(DEFAULT_ROLE_ID)
                    .build());
            List<MultiFile> multiFileList = new ArrayList<>();
            multiFileList.add(MultiFile.builder()
                    .userId(userId)
                    .fileName(IMG.getCurrentPath())
                    .fileFullPath(userId + "/" + IMG.getPath())
                    .fileNameOrigin("img")
                    .deletableFlag(false)
                    .createUser(loginUserId)
                    .createTime(createTime)
                    .build());
            multiFileList.add(MultiFile.builder()
                    .userId(userId)
                    .fileName(AUDIO.getCurrentPath())
                    .fileFullPath(userId + "/" + AUDIO.getPath())
                    .fileNameOrigin("audio")
                    .deletableFlag(false)
                    .createUser(loginUserId)
                    .createTime(createTime)
                    .build());
            multiFileService.saveBatch(multiFileList);
            multiFileList.add(MultiFile.builder()
                    .userId(userId)
                    .parentId(multiFileList.get(0).getId())
                    .fileName(IMG_AVATAR.getCurrentPath())
                    .fileFullPath(userId + "/" + IMG_AVATAR.getPath())
                    .fileNameOrigin("avatar")
                    .deletableFlag(false)
                    .createUser(loginUserId)
                    .createTime(createTime)
                    .build());
            multiFileList.add(MultiFile.builder()
                    .userId(userId)
                    .parentId(multiFileList.get(1).getId())
                    .fileName(AUDIO_CHAT.getCurrentPath())
                    .fileFullPath(userId + "/" + AUDIO_CHAT.getPath())
                    .fileNameOrigin("chat")
                    .deletableFlag(false)
                    .createUser(loginUserId)
                    .createTime(createTime)
                    .build());
            multiFileList.remove(0);
            multiFileList.remove(0);
            multiFileService.saveBatch(multiFileList);
        } else {
            if (loginUser.getRoleWeight() > 100 && ROOT_USER_ID_LIST.contains(user.getId()))
                throw new IllegalRequestException();
            String avatar = userMapper.selectBackUserAvatarById(null, null, user.getId());
            if (avatar == null)
                throw new IllegalRequestException();
            if (user.getEmail() != null) {
                if (!RegexUtil.checkEmail(user.getEmail()))
                    throw new OperationStatusException();
                if (userMapper.selectBackUserAvatarById(user.getEmail(), null, null) != null)
                    throw new OperationStatusException("邮箱已存在!");
            }
            Date updateTime = new Date();
            if (user.getAvatar() != null) {
                if (!user.getAvatar().startsWith(STATIC_RESOURCE_URL))
                    user.setAvatar("");
                multiFileService.updateUserAvatarBy(loginUserId, avatar.split(STATIC_RESOURCE_URL)[1], updateTime);
            }
            user.setUpdateUser(loginUserId);
            user.setUpdateTime(updateTime);
            userMapper.updateById(user);
        }
    }

    @Override
    @Transactional
    public void deleteBackUserByIdList(List<Integer> idList) {
        // TODO: 物理删除用户牵扯太多数据, 延后处理
        if (idList.isEmpty())
            throw new IllegalRequestException();
    }

    @Override
    @Transactional
    public void updateUsersStatusBackVO(StatusBackVO statusBackVO) {
        // TODO: 删除用户牵扯太多数据, 延后处理
    }

    @Override
    @Transactional
    public void updateUserVO(UserVO userVO) {
        Integer loginUserId = UserUtil.getLoginUser().getUserId();
        userMapper.update(null, new LambdaUpdateWrapper<User>()
                .set(userVO.getIntro() != null, User::getIntro, userVO.getIntro())
                .set(userVO.getGender() != null, User::getGender, userVO.getGender())
                .set(userVO.getWebsite() != null, User::getWebsite, userVO.getWebsite())
                .set(userVO.getNickname() != null, User::getNickname, userVO.getNickname())
                .set(User::getUpdateUser, loginUserId)
                .set(User::getUpdateTime, new Date())
                .eq(User::getId, loginUserId));
    }

    @Override
    public PagePojo<UsersBackDTO> getUsersBackDTO(ConditionBackVO condition) {
        LoginUser loginUser = UserUtil.getLoginUser();
        if (DELETED.equals(condition.getType()) && loginUser.getRoleWeight() > 100)
            return new PagePojo<>();
        Integer count = userMapper.selectUsersBackDTOCount(condition);
        if (count == 0)
            return new PagePojo<>();
        condition.setCurrent((condition.getCurrent() - 1) * condition.getSize());
        List<UsersBackDTO> usersBackDTOList = userMapper.selectUsersBackDTO(condition);
        return new PagePojo<>(count, usersBackDTOList);
    }

    @Override
    public boolean getBackUserExistFlag(String email, String username) {
        if (CommonUtil.isEmpty(email) && CommonUtil.isEmpty(username))
            return false;
        return userMapper.selectBackUserAvatarById(email, username, null) != null;
    }

    @Override
    public void deleteBackUserOnlinesByIdList(List<Integer> idList) {
        if (idList.isEmpty() || !Collections.disjoint(idList, ROOT_USER_ID_LIST))
            throw new OperationStatusException();
        List<Object> loginUserList = sessionRegistry.getAllPrincipals().stream().filter(e -> {
            LoginUser loginUser = (LoginUser) e;
            return idList.contains(loginUser.getUserId());
        }).collect(Collectors.toList());
        List<SessionInformation> allSessions = new ArrayList<>();
        loginUserList.forEach(e -> allSessions.addAll(sessionRegistry.getAllSessions(e, false)));
        allSessions.forEach(SessionInformation::expireNow);
    }

    @Override
    public PagePojo<UserOnlinesBackDTO> getUserOnlinesBackDTO(ConditionBackVO condition) {
        List<Integer> onlineUserIdList = sessionRegistry.getAllPrincipals().stream()
                .filter(e -> sessionRegistry.getAllSessions(e, false).size() > 0)
                .map(e -> BeanCopyUtil.copyObject(e, LoginUser.class))
                .filter(e -> CommonUtil.isEmpty(condition.getKeywords()) || e.getUsername().contains(condition.getKeywords()))
                .filter(e -> condition.getFlag() == null || e.getLoginPlatform().equals(condition.getFlag()))
                .sorted(Comparator.comparing(LoginUser::getLoginTime).reversed())
                .map(LoginUser::getUserId)
                .collect(Collectors.toList());
        int count = onlineUserIdList.size();
        if (count == 0)
            return new PagePojo<>();
        int current = (condition.getCurrent() - 1) * condition.getSize();
        if (current >= count)
            return new PagePojo<>(count, new ArrayList<>());
        int size = count > condition.getSize() ? current + condition.getSize() : count;
        List<Integer> idList = onlineUserIdList.subList(current, size);
        List<UserOnlinesBackDTO> userOnlinesBackDTOList = userMapper.selectUserOnlinesBackDTO(idList);
        return new PagePojo<>(count, userOnlinesBackDTOList);
    }
}




