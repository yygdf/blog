package com.iksling.blog.handler;

import com.alibaba.fastjson.JSON;
import com.iksling.blog.pojo.Result;
import com.iksling.blog.util.LocaleUtil;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.iksling.blog.constant.StatusConst.*;

@Component
public class AuthenticationFailHandlerImpl implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException {
        int code = AUTHENTICATION_FAILURE;
        String message = e.getMessage();
        if (e instanceof InternalAuthenticationServiceException) {
            if (e.getCause() instanceof LockedException)
                code = ACCOUNT_LOCKED;
            else if (e.getCause() instanceof DisabledException)
                code = ACCOUNT_DISABLED;
        } else
            message = LocaleUtil.getMessageByLang("H0007", httpServletRequest.getHeader("Lang"));
        httpServletResponse.setContentType("application/json;charset=UTF-8");
        httpServletResponse.getWriter().write(JSON.toJSONString(Result.failure().code(code).message(message)));
    }
}
