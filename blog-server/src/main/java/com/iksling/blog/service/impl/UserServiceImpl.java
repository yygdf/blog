package com.iksling.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iksling.blog.dto.UsersBackDTO;
import com.iksling.blog.dto.UsersOnlineBackDTO;
import com.iksling.blog.entity.MultiDir;
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
import com.iksling.blog.service.MultiDirService;
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
import static com.iksling.blog.enums.FilePathEnum.*;

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
    private MultiDirService multiDirService;
    @Autowired
    private MultiFileService multiFileService;

    @Autowired
    private SessionRegistry sessionRegistry;

    @Override
    @Transactional
    public void saveOrUpdateUserBackVO(UserBackVO userBackVO) {
        LoginUser loginUser = UserUtil.getLoginUser();
        User user = BeanCopyUtil.copyObject(userBackVO, User.class);
        if (user.getId() == null) {
            if (userBackVO.getUsername() == null || user.getNickname() == null || !RegexUtil.checkEmail(user.getEmail()))
                throw new OperationStatusException();
            if (userMapper.selectBackUserAvatarById(user.getEmail(), userBackVO.getUsername(), null) != null)
                throw new OperationStatusException("用户已存在!");
            Date createTime = new Date();
            Integer loginUserId = loginUser.getUserId();
            user.setAvatar(null);
            user.setCreateUser(loginUserId);
            user.setCreateTime(createTime);
            userMapper.insert(user);
            userAuthMapper.insert(UserAuth.builder()
                    .userId(user.getId())
                    .username(userBackVO.getUsername().trim())
                    .password(DEFAULT_PASSWORD)
                    .createUser(loginUserId)
                    .createTime(createTime)
                    .build());
            userRoleMapper.insert(UserRole.builder()
                    .userId(user.getId())
                    .roleId(DEFAULT_ROLE_ID)
                    .build());
            List<MultiDir> multiDirList = new ArrayList<>();
            multiDirList.add(MultiDir.builder()
                    .userId(user.getId())
                    .dirPath(Long.valueOf(IMG.getMark()))
                    .dirName(IMG.getCurrentPath())
                    .deletableFlag(false)
                    .createUser(loginUserId)
                    .createTime(createTime)
                    .build());
            multiDirList.add(MultiDir.builder()
                    .userId(user.getId())
                    .dirPath(Long.valueOf(AUDIO.getMark()))
                    .dirName(AUDIO.getCurrentPath())
                    .deletableFlag(false)
                    .createUser(loginUserId)
                    .createTime(createTime)
                    .build());
            multiDirService.saveBatch(multiDirList);
            multiDirList.add(MultiDir.builder()
                    .userId(user.getId())
                    .parentId(multiDirList.get(0).getId())
                    .dirPath(Long.valueOf(IMG_AVATAR.getMark()))
                    .dirName(IMG_AVATAR.getCurrentPath())
                    .deletableFlag(false)
                    .createUser(loginUserId)
                    .createTime(createTime)
                    .build());
            multiDirList.add(MultiDir.builder()
                    .userId(user.getId())
                    .parentId(multiDirList.get(1).getId())
                    .dirPath(Long.valueOf(AUDIO_CHAT.getMark()))
                    .dirName(AUDIO_CHAT.getCurrentPath())
                    .deletableFlag(false)
                    .createUser(loginUserId)
                    .createTime(createTime)
                    .build());
            multiDirList.remove(0);
            multiDirList.remove(0);
            multiDirService.saveBatch(multiDirList);
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
            if (user.getAvatar() != null) {
                if (!user.getAvatar().startsWith(STATIC_RESOURCE_URL))
                    user.setAvatar("");
                if (avatar.startsWith(STATIC_RESOURCE_URL + user.getId() + "/" + IMG_AVATAR.getPath()))
                    multiFileService.updateUserAvatarBy(user.getId(), CommonUtil.getSplitStringByIndex(avatar, "/", -1));
            }
            user.setUpdateUser(loginUser.getUserId());
            user.setUpdateTime(new Date());
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
    public void deleteBackUsersOnlineByIdList(List<Integer> idList) {
        if (idList.isEmpty() || idList.contains(ROOT_USER_ID) || (UserUtil.getLoginUser().getRoleWeight() > 100 && !Collections.disjoint(idList, ROOT_USER_ID_LIST)))
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
    public PagePojo<UsersOnlineBackDTO> getUsersOnlineBackDTO(ConditionBackVO condition) {
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
        List<UsersOnlineBackDTO> usersOnlineBackDTOList = userMapper.selectUserOnlinesBackDTO(idList);
        return new PagePojo<>(count, usersOnlineBackDTOList);
    }

    @Override
    @Transactional
    public void updateUserVO(UserVO userVO) {
        userMapper.update(null, new LambdaUpdateWrapper<User>()
                .set(userVO.getIntro() != null, User::getIntro, userVO.getIntro())
                .set(userVO.getWebsite() != null, User::getWebsite, userVO.getWebsite())
                .set(userVO.getNickname() != null, User::getNickname, userVO.getNickname())
                .eq(User::getId, UserUtil.getLoginUser().getUserId()));
    }
}




