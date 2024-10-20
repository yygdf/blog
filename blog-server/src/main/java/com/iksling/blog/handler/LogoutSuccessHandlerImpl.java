package com.iksling.blog.handler;

import com.alibaba.fastjson.JSON;
import com.iksling.blog.pojo.Result;
import com.iksling.blog.util.JwtUtil;
import com.iksling.blog.util.LocaleUtil;
import com.iksling.blog.util.RedisUtil;
import io.jsonwebtoken.Claims;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.iksling.blog.constant.RedisConst.LOGIN_TOKEN;

@Component
public class LogoutSuccessHandlerImpl implements LogoutSuccessHandler {
    @Override
    public void onLogoutSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException {
        String token = httpServletRequest.getHeader("Token");
        String lang = httpServletRequest.getHeader("Lang");
        Claims claims = JwtUtil.parseJwtToken(token);
        String subject = claims.getSubject();
        RedisUtil.delKey(LOGIN_TOKEN + "_" + subject);
        httpServletResponse.setContentType("application/json;charset=UTF-8");
        httpServletResponse.getWriter().write(JSON.toJSONString(Result.success().message(LocaleUtil.getMessageByLang("H0006", lang))));
    }
}
