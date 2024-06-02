package com.iksling.blog.handler;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.iksling.blog.dto.LoginUserBackDTO;
import com.iksling.blog.dto.LoginUserDTO;
import com.iksling.blog.entity.LoginLog;
import com.iksling.blog.entity.User;
import com.iksling.blog.entity.UserAuth;
import com.iksling.blog.mapper.LoginLogMapper;
import com.iksling.blog.mapper.UserAuthMapper;
import com.iksling.blog.mapper.UserMapper;
import com.iksling.blog.pojo.LoginUser;
import com.iksling.blog.pojo.Result;
import com.iksling.blog.util.IpUtil;
import com.iksling.blog.util.RedisUtil;
import com.iksling.blog.util.UserUtil;
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
import java.util.Set;

import static com.iksling.blog.constant.RedisConst.ARTICLE_USER_LIKE;
import static com.iksling.blog.constant.RedisConst.COMMENT_USER_LIKE;

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
        if (loginPlatform) {
            LoginUserBackDTO loginUserBackDTO = LoginUserBackDTO.builder()
                    .userId(userId)
                    .intro(user.getIntro())
                    .email(user.getEmail())
                    .avatar(user.getAvatar())
                    .gender(user.getGender())
                    .weight(loginUser.getRoleWeight())
                    .website(user.getWebsite())
                    .nickname(user.getNickname())
                    .modifiedFlag(user.getModifiedFlag())
                    .commentLikeSet(commentLikeSet)
                    .build();
            httpServletResponse.setContentType("application/json;charset=UTF-8");
            httpServletResponse.getWriter().write(JSON.toJSONString(Result.success().message("登录成功!").data(loginUserBackDTO)));
        } else {
            Set<Integer> articleLikeSet = RedisUtil.getMapValue(ARTICLE_USER_LIKE, userId.toString());
            LoginUserDTO loginUserDTO = LoginUserDTO.builder()
                    .userId(userId)
                    .intro(user.getIntro())
                    .email(user.getEmail())
                    .avatar(user.getAvatar())
                    .gender(user.getGender())
                    .website(user.getWebsite())
                    .nickname(user.getNickname())
                    .articleLikeSet(articleLikeSet)
                    .commentLikeSet(commentLikeSet)
                    .build();
            httpServletResponse.setContentType("application/json;charset=UTF-8");
            httpServletResponse.getWriter().write(JSON.toJSONString(Result.success().message("登录成功!").data(loginUserDTO)));
        }
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
