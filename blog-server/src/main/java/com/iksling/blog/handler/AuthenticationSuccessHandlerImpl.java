package com.iksling.blog.handler;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.iksling.blog.dto.LoginUserDTO;
import com.iksling.blog.entity.LoginLog;
import com.iksling.blog.entity.User;
import com.iksling.blog.entity.UserAuth;
import com.iksling.blog.mapper.LoginLogMapper;
import com.iksling.blog.mapper.UserAuthMapper;
import com.iksling.blog.mapper.UserMapper;
import com.iksling.blog.pojo.Dict;
import com.iksling.blog.pojo.LoginUser;
import com.iksling.blog.pojo.Result;
import com.iksling.blog.util.*;
import eu.bitwalker.useragentutils.UserAgent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static com.iksling.blog.constant.RedisConst.*;

@Component
public class AuthenticationSuccessHandlerImpl implements AuthenticationSuccessHandler {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserAuthMapper userAuthMapper;
    @Autowired
    private LoginLogMapper loginLogMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException {
        LoginUser loginUser = UserUtil.getLoginUser();
        Integer userId = loginUser.getUserId();
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>()
                .select(User::getIntro, User::getEmail, User::getAvatar, User::getGender, User::getWebsite, User::getNickname, User::getModifiedFlag)
                .eq(User::getId, userId));
        Boolean loginPlatform = Boolean.parseBoolean(httpServletRequest.getHeader("Login-Platform"));
        Date loginTime = new Date();
        loginUser.setLoginTime(loginTime);
        loginUser.setLoginPlatform(loginPlatform);
        insertLoginLog(userId, loginTime, loginPlatform, httpServletRequest);
        Set<Integer> commentLikeSet = RedisUtil.getMapValue(COMMENT_USER_LIKE, userId.toString());
        Set<Integer> articleLikeSet = RedisUtil.getMapValue(ARTICLE_USER_LIKE, userId.toString());
        LoginUserDTO loginUserDTO = LoginUserDTO.builder()
                .userId(userId)
                .intro(user.getIntro())
                .email(user.getEmail())
                .avatar(user.getAvatar())
                .gender(user.getGender())
                .weight(loginUser.getRoleWeight())
                .website(user.getWebsite())
                .nickname(user.getNickname())
                .modifiedFlag(user.getModifiedFlag())
                .articleLikeSet(articleLikeSet)
                .commentLikeSet(commentLikeSet)
                .build();
        String tokenId = String.valueOf(IdWorker.getId());
        Map<String, Object> map = new HashMap<>();
        map.put("tokenId", tokenId);
        map.put("userId", userId);
        map.put("username", loginUser.getUsername());
        map.put("loginTime", loginUser.getLoginTime());
        map.put("loginPlatform", loginUser.getLoginPlatform());
        map.put("roleIdList", loginUser.getRoleIdList());
        map.put("roleWeight", loginUser.getRoleWeight());
        RedisUtil.setMap(LOGIN_TOKEN + "_" + userId, map);
        RedisUtil.expire(LOGIN_TOKEN + "_" + userId, TOKEN_EXPIRE_TIME, TimeUnit.MILLISECONDS);
        httpServletResponse.setContentType("application/json;charset=UTF-8");
        httpServletResponse.getWriter().write(JSON.toJSONString(Result.success().message(LocaleUtil.getMessageByLang("C0007", httpServletRequest.getHeader("Lang"))).data(Dict.create().set("loginUserDTO", loginUserDTO).set("token", JwtUtil.createJwtToken(tokenId, userId.toString())))));
    }

    @Async
    public void insertLoginLog(Integer userId, Date loginTime, Boolean loginPlatform, HttpServletRequest httpServletRequest) {
        UserAgent userAgent = UserAgent.parseUserAgentString(httpServletRequest.getHeader("User-Agent"));
        String ipAddress = IpUtil.getIpAddress(httpServletRequest);
        String ipSource = IpUtil.getIpSource(ipAddress);
        LoginLog loginLog = LoginLog.builder()
                .userId(userId)
                .loginTime(loginTime)
                .loginDevice(userAgent.getOperatingSystem().getDeviceType().getName())
                .loginPlatform(loginPlatform)
                .loginSystem(userAgent.getOperatingSystem().getName())
                .loginBrowser(userAgent.getBrowser().getName())
                .ipSource(ipSource)
                .ipAddress(ipAddress)
                .build();
        loginLogMapper.insert(loginLog);
        userAuthMapper.update(null, new LambdaUpdateWrapper<UserAuth>()
                .set(UserAuth::getLoginLogId, loginLog.getId())
                .eq(UserAuth::getUserId, userId));
    }
}
