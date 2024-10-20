package com.iksling.blog.filter;

import com.alibaba.fastjson.JSON;
import com.iksling.blog.pojo.LoginUser;
import com.iksling.blog.pojo.Result;
import com.iksling.blog.util.CommonUtil;
import com.iksling.blog.util.JwtUtil;
import com.iksling.blog.util.LocaleUtil;
import com.iksling.blog.util.RedisUtil;
import io.jsonwebtoken.Claims;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.iksling.blog.constant.CommonConst.ADMIN_CONTACT_QQ;
import static com.iksling.blog.constant.RedisConst.LOGIN_TOKEN;
import static com.iksling.blog.constant.RedisConst.TOKEN_EXPIRE_TIME;
import static com.iksling.blog.constant.StatusConst.*;

@Component
public class JwtTokenAuthenticationFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        String token = httpServletRequest.getHeader("Token");
        if (CommonUtil.isEmpty(token)) {
            filterChain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }
        Claims claims;
        String lang = httpServletRequest.getHeader("Lang");
        try {
            claims = JwtUtil.parseJwtToken(token);
        } catch (Exception e) {
            httpServletResponse.setContentType("application/json;charset=UTF-8");
            httpServletResponse.getWriter().write(JSON.toJSONString(Result.failure().code(AUTHENTICATION_FAILURE).message(LocaleUtil.getMessageByLang("F0001", lang))));
            return;
        }
        String subject = claims.getSubject();
        Map<String, Object> map = RedisUtil.getMap(LOGIN_TOKEN + "_" + subject);
        if (map.isEmpty()) {
            httpServletResponse.setContentType("application/json;charset=UTF-8");
            httpServletResponse.getWriter().write(JSON.toJSONString(Result.failure().code(INVALID_SESSION).message(LocaleUtil.getMessageByLang("F0002", lang))));
            return;
        }
        if (!claims.getId().equals(map.get("tokenId"))) {
            httpServletResponse.setContentType("application/json;charset=UTF-8");
            httpServletResponse.getWriter().write(JSON.toJSONString(Result.failure().code(EXPIRED_SESSION).message(LocaleUtil.getMessageByLang("F0003", lang))));
            return;
        }
        if (map.get("offlineFlag") != null) {
            RedisUtil.delKey(LOGIN_TOKEN + "_" + subject);
            httpServletResponse.setContentType("application/json;charset=UTF-8");
            httpServletResponse.getWriter().write(JSON.toJSONString(Result.failure().code(NOT_LOGIN).message(LocaleUtil.getMessageByLang("F0004", lang, ADMIN_CONTACT_QQ))));
            return;
        }
        LoginUser loginUser = LoginUser.builder()
                .userId((Integer) map.get("userId"))
                .username(map.get("username").toString())
                .loginTime((Date) map.get("loginTime"))
                .loginPlatform((Boolean) map.get("loginPlatform"))
                .roleIdList((List<String>) map.get("roleIdList"))
                .roleWeight((Integer) map.get("roleWeight"))
                .build();
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(loginUser, null, loginUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);
        RedisUtil.expire(LOGIN_TOKEN + "_" + loginUser.getUserId(), TOKEN_EXPIRE_TIME, TimeUnit.MILLISECONDS);
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
