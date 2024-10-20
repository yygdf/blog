package com.iksling.blog.handler;

import com.alibaba.fastjson.JSON;
import com.iksling.blog.pojo.Result;
import com.iksling.blog.util.LocaleUtil;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.iksling.blog.constant.StatusConst.FAILURE;
import static com.iksling.blog.constant.StatusConst.NOT_LOGIN;

@Component
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException {
        int code = NOT_LOGIN;
        String message = LocaleUtil.getMessage("H0002");
        if (e instanceof AuthenticationServiceException) {
            code = FAILURE;
            message = e.getMessage();
        }
        httpServletResponse.setContentType("application/json;charset=utf-8");
        httpServletResponse.getWriter().write(JSON.toJSONString(Result.failure().code(code).message(message)));
    }
}
