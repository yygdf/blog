package com.iksling.blog.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.iksling.blog.entity.Role;
import com.iksling.blog.entity.UserAuth;
import com.iksling.blog.mapper.RoleMapper;
import com.iksling.blog.mapper.UserAuthMapper;
import com.iksling.blog.pojo.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.iksling.blog.constant.CommonConst.ADMIN_CONTACT_QQ;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserAuthMapper userAuthMapper;
    @Autowired
    private RoleMapper roleMapper;

    @Override
    public UserDetails loadUserByUsername(String username) {
        if (StringUtils.isBlank(username))
            throw new UsernameNotFoundException("用户名为空!");
        UserAuth userAuth = userAuthMapper.selectOne(new LambdaQueryWrapper<UserAuth>()
            .select(UserAuth::getUserId, UserAuth::getUsername, UserAuth::getPassword, UserAuth::getLockedFlag, UserAuth::getDisabledFlag)
            .eq(UserAuth::getUsername, username.trim())
            .eq(UserAuth::getDeletedFlag, false));
        if (userAuth == null)
            throw new UsernameNotFoundException("用户名不存在!");
        if (userAuth.getLockedFlag())
            throw new LockedException("您的账号已被锁定, 如有疑问请联系管理员[QQ: " + ADMIN_CONTACT_QQ + "]");
        if (userAuth.getDisabledFlag())
            throw new DisabledException("您的账号已被禁用, 如有疑问请联系管理员[QQ: " + ADMIN_CONTACT_QQ + "]");
        List<Role> roleList = roleMapper.selectLoginRoleByUserId(userAuth.getUserId());
        if (roleList.isEmpty())
            throw new DisabledException("您的角色已被禁用, 如有疑问请联系管理员[QQ: " + ADMIN_CONTACT_QQ + "]");
        return LoginUser.builder()
                .userId(userAuth.getUserId())
                .username(userAuth.getUsername())
                .password(userAuth.getPassword())
                .roleWeight(roleList.get(0).getRoleWeight())
                .roleIdList(roleList.stream().map(e -> e.getId().toString()).collect(Collectors.toList()))
                .build();
    }
}
