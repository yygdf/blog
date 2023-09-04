package com.iksling.blog.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.iksling.blog.entity.UserAuth;
import com.iksling.blog.mapper.RoleMapper;
import com.iksling.blog.mapper.UserAuthMapper;
import com.iksling.blog.pojo.LoginRole;
import com.iksling.blog.pojo.LoginUser;
import com.iksling.blog.util.IpUtil;
import eu.bitwalker.useragentutils.UserAgent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.iksling.blog.constant.CommonConst.ADMIN_CONTACT;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserAuthMapper userAuthMapper;
    @Autowired
    private RoleMapper roleMapper;

    @Resource
    private HttpServletRequest request;

    @Override
    public UserDetails loadUserByUsername(String username) {
        if (StringUtils.isBlank(username))
            throw new UsernameNotFoundException("用户名为空!");
        UserAuth userAuth = userAuthMapper.selectOne(new LambdaQueryWrapper<UserAuth>()
            .select(UserAuth::getId, UserAuth::getUserId, UserAuth::getUsername, UserAuth::getPassword, UserAuth::getLockedFlag, UserAuth::getDisabledFlag)
            .eq(UserAuth::getUsername, username)
            .eq(UserAuth::getDeletedFlag, false));
        if (Objects.isNull(userAuth))
            throw new UsernameNotFoundException("用户名不存在!");
        if (userAuth.getLockedFlag())
            throw new LockedException("您的账号已被锁定, 如有疑问请联系管理员[" + ADMIN_CONTACT + "]");
        if (userAuth.getDisabledFlag())
            throw new DisabledException("您的账号已被禁用, 如有疑问请联系管理员[" + ADMIN_CONTACT + "]");
        List<LoginRole> roleList = roleMapper.listLoginRoleByUserId(userAuth.getUserId());
        if (CollectionUtils.isEmpty(roleList))
            throw new DisabledException("您的角色已被禁用, 如有疑问请联系管理员[" + ADMIN_CONTACT + "]");
        UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
        String loginPlatform = request.getHeader("Login-Platform");
        String ipAddress = IpUtil.getIpAddress(request);
        String ipSource = IpUtil.getIpSource(ipAddress);
        return LoginUser.builder()
                .id(userAuth.getId())
                .userId(userAuth.getUserId())
                .username(userAuth.getUsername())
                .password(userAuth.getPassword())
                .loginTime(new Date())
                .loginDevice(userAgent.getOperatingSystem().getDeviceType().getName())
                .loginMethod(1)
                .loginPlatform(Boolean.parseBoolean(loginPlatform))
                .ipAddress(ipAddress)
                .ipSource(ipSource)
                .roleWeight(roleList.get(0).getRoleWeight())
                .roleList(roleList.stream()
                        .map(LoginRole::getRoleName)
                        .collect(Collectors.toList()))
                .build();
    }
}
