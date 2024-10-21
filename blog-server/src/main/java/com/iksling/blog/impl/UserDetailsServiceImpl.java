package com.iksling.blog.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.iksling.blog.entity.Role;
import com.iksling.blog.entity.UserAuth;
import com.iksling.blog.mapper.RoleMapper;
import com.iksling.blog.mapper.UserAuthMapper;
import com.iksling.blog.pojo.LoginUser;
import com.iksling.blog.util.CommonUtil;
import com.iksling.blog.util.LocaleUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

import static com.iksling.blog.constant.CommonConst.ADMIN_CONTACT_QQ;

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
        String lang = request.getHeader("Lang");
        if (CommonUtil.isEmpty(username))
            throw new UsernameNotFoundException(LocaleUtil.getMessageByLang("I0001", lang));
        UserAuth userAuth = userAuthMapper.selectOne(new LambdaQueryWrapper<UserAuth>()
            .select(UserAuth::getUserId, UserAuth::getUsername, UserAuth::getPassword, UserAuth::getLockedFlag, UserAuth::getDisabledFlag)
            .eq(UserAuth::getUsername, username)
            .eq(UserAuth::getDeletedFlag, false));
        if (userAuth == null)
            throw new UsernameNotFoundException(LocaleUtil.getMessageByLang("I0001", lang));
        if (userAuth.getLockedFlag())
            throw new LockedException(LocaleUtil.getMessageByLang("A0001", lang, username, ADMIN_CONTACT_QQ));
        if (userAuth.getDisabledFlag())
            throw new DisabledException(LocaleUtil.getMessageByLang("I0002", lang, username, ADMIN_CONTACT_QQ));
        List<Role> roleList = roleMapper.selectLoginRoleByUserId(userAuth.getUserId());
        if (roleList.isEmpty())
            throw new DisabledException(LocaleUtil.getMessageByLang("I0003", lang, username, ADMIN_CONTACT_QQ));
        return LoginUser.builder()
                .userId(userAuth.getUserId())
                .username(userAuth.getUsername())
                .password(userAuth.getPassword())
                .roleWeight(roleList.get(0).getRoleWeight())
                .roleIdList(roleList.stream().map(e -> e.getId().toString()).collect(Collectors.toList()))
                .build();
    }
}
