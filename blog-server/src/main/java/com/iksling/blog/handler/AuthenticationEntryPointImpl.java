package com.iksling.blog.handler;

import com.alibaba.fastjson.JSON;
import com.iksling.blog.constant.StatusConst;
import com.iksling.blog.pojo.Result;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException {
        int code = StatusConst.NOT_LOGIN;
        String message = "请先登录!";
        if (e instanceof AuthenticationServiceException) {
            code = StatusConst.FAILURE;
            message = e.getMessage();
        }
        httpServletResponse.setContentType("application/json;charset=utf-8");
        httpServletResponse.getWriter().write(JSON.toJSONString(Result.failure().code(code).message(message)));
    }
}
