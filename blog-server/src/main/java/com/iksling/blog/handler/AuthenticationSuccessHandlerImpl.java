package com.iksling.blog.handler;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.iksling.blog.pojo.LoginUser;
import com.iksling.blog.entity.LoginLog;
import com.iksling.blog.entity.User;
import com.iksling.blog.entity.UserAuth;
import com.iksling.blog.mapper.LoginLogMapper;
import com.iksling.blog.mapper.UserMapper;
import com.iksling.blog.dto.LoginUserDTO;
import com.iksling.blog.pojo.Result;
import com.iksling.blog.mapper.UserAuthMapper;
import com.iksling.blog.util.BeanCopyUtil;
import com.iksling.blog.util.UserUtil;
import eu.bitwalker.useragentutils.UserAgent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

import static com.iksling.blog.constant.RedisConst.ARTICLE_USER_LIKE;
import static com.iksling.blog.constant.RedisConst.COMMENT_USER_LIKE;

@Component
public class AuthenticationSuccessHandlerImpl implements AuthenticationSuccessHandler {
    @Autowired
    private UserAuthMapper userAuthMapper;
    @Autowired
    private LoginLogMapper loginLogMapper;
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException {
        LoginUser loginUser = UserUtil.getLoginUser();
        updateUserAuth(loginUser);
        insertLoginLog(loginUser, httpServletRequest);
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>()
                .select(User::getId, User::getNickname, User::getAvatar, User::getIntro, User::getEmail, User::getWebsite)
                .eq(User::getId, loginUser.getUserId()));
        Set<Integer> articleLikeSet = (Set<Integer>) redisTemplate.boundHashOps(ARTICLE_USER_LIKE).get(user.getId());
        Set<Integer> commentLikeSet = (Set<Integer>) redisTemplate.boundHashOps(COMMENT_USER_LIKE).get(user.getId());
        LoginUserDTO loginUserDTO = LoginUserDTO.builder()
                .userId(user.getId())
                .intro(user.getIntro())
                .email(user.getEmail())
                .avatar(user.getAvatar())
                .website(user.getWebsite())
                .nickname(user.getNickname())
                .articleLikeSet(articleLikeSet)
                .commentLikeSet(commentLikeSet)
                .build();
        httpServletResponse.setContentType("application/json;charset=UTF-8");
        httpServletResponse.getWriter().write(JSON.toJSONString(Result.success().message("登录成功!").data(loginUserDTO)));
    }

    @Async
    public void updateUserAuth(LoginUser loginUser) {
        userAuthMapper.updateById(UserAuth.builder()
                .id(loginUser.getId())
                .loginTime(loginUser.getLoginTime())
                .loginDevice(loginUser.getLoginDevice())
                .ipSource(loginUser.getIpSource())
                .ipAddress(loginUser.getIpAddress())
                .build());
    }

    @Async
    public void insertLoginLog(LoginUser loginUser, HttpServletRequest request) {
        UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
        loginLogMapper.insert(LoginLog.builder()
                .userId(loginUser.getUserId())
                .loginType(loginUser.getLoginType())
                .loginTime(loginUser.getLoginTime())
                .loginDevice(loginUser.getLoginDevice())
                .loginBrowser(userAgent.getBrowser().getName())
                .loginSystem(userAgent.getOperatingSystem().getName())
                .ipSource(loginUser.getIpSource())
                .ipAddress(loginUser.getIpAddress())
                .build());
    }
}
