package com.iksling.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iksling.blog.dto.UsersBackDTO;
import com.iksling.blog.entity.User;
import com.iksling.blog.entity.UserAuth;
import com.iksling.blog.exception.IllegalRequestException;
import com.iksling.blog.mapper.UserAuthMapper;
import com.iksling.blog.mapper.UserMapper;
import com.iksling.blog.pojo.LoginUser;
import com.iksling.blog.pojo.PagePojo;
import com.iksling.blog.service.UserService;
import com.iksling.blog.util.RegexUtil;
import com.iksling.blog.util.UserUtil;
import com.iksling.blog.vo.ConditionVO;
import com.iksling.blog.vo.UserBackVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import static com.iksling.blog.constant.CommonConst.DEFAULT_AVATAR;
import static com.iksling.blog.constant.CommonConst.DEFAULT_PASSWORD;

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

    @Override
    public PagePojo<UsersBackDTO> getPageUsersBackDTO(ConditionVO condition) {
        if (UserUtil.getLoginUser().getRoleWeight() > 100 && Objects.equals(condition.getDeletedFlag(), true))
            throw new IllegalRequestException();
        condition.setCurrent((condition.getCurrent() - 1) * condition.getSize());
        List<UsersBackDTO> usersBackDTOList = userMapper.listUsersBackDTO(condition);
        if (CollectionUtils.isEmpty(usersBackDTOList))
            return new PagePojo<>();
        return new PagePojo<>(usersBackDTOList.size(), usersBackDTOList);
    }

    @Override
    @Transactional
    public void deleteUserIdList(List<Integer> userIdList) {

    }

    @Override
    @Transactional
    public void saveOrUpdateUserBackVO(UserBackVO userBackVO) {
        LoginUser loginUser = UserUtil.getLoginUser();
        User user = User.builder()
                .id(userBackVO.getId())
                .email(userBackVO.getEmail().trim())
                .nickname(userBackVO.getNickname().trim())
                .build();
        if (StringUtils.isBlank(userBackVO.getAvatar()))
            user.setAvatar(DEFAULT_AVATAR);
        else
            user.setAvatar(userBackVO.getAvatar().trim());
        if (Objects.nonNull(userBackVO.getIntro()))
            user.setIntro(userBackVO.getIntro().trim());
        if (Objects.nonNull(userBackVO.getWebsite()))
            user.setWebsite(userBackVO.getWebsite().trim());
        if (Objects.isNull(userBackVO.getId())) {
            if (StringUtils.isBlank(userBackVO.getUsername()) || !RegexUtil.checkEmail(userBackVO.getEmail()))
                throw new IllegalRequestException();
            Integer count = userAuthMapper.selectCount(new LambdaQueryWrapper<UserAuth>()
                    .eq(UserAuth::getUsername, userBackVO.getUsername().trim()));
            if (count > 0)
                throw new IllegalRequestException();
            count = userMapper.selectCount(new LambdaQueryWrapper<User>()
                    .eq(User::getEmail, user.getEmail()));
            if (count > 0)
                throw new IllegalRequestException();
            user.setCreateUser(loginUser.getUserId());
            user.setCreateTime(new Date());
            userMapper.insert(user);
            userAuthMapper.insert(UserAuth.builder()
                    .userId(user.getId())
                    .username(userBackVO.getUsername().trim())
                    .password(DEFAULT_PASSWORD)
                    .createUser(loginUser.getUserId())
                    .createTime(new Date())
                    .build());
        } else {
            Integer count = userMapper.selectCount(new LambdaQueryWrapper<User>()
                    .eq(User::getEmail, user.getEmail())
                    .ne(User::getId, user.getId()));
            if (count > 0)
                throw new IllegalRequestException();
            user.setUpdateUser(loginUser.getUserId());
            user.setUpdateTime(new Date());
            userMapper.updateById(user);
        }
    }

    @Override
    public boolean getBackUserExistFlag(String keywords) {
        if (StringUtils.isBlank(keywords.trim()))
            return false;
        return userMapper.selectCount(new LambdaQueryWrapper<User>()
                .eq(User::getEmail, keywords.trim())) != 0;
    }
}




