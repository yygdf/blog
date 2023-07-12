package com.iksling.blog.handler;

import com.alibaba.fastjson.JSON;
import com.iksling.blog.constant.CommonConst;
import com.iksling.blog.constant.StatusConst;
import com.iksling.blog.pojo.Result;
import com.iksling.blog.util.UserUtil;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AuthenticationFailHandlerImpl implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException {
        httpServletResponse.setContentType("application/json;charset=UTF-8");
        int code = StatusConst.AUTHENTICATION_FAILURE;
        String message = e.getMessage();
        String username = UserUtil.getLoginUser().getUsername();
        if (e instanceof LockedException) {
            code = StatusConst.ACCOUNT_LOCKED;
            message = "账户[" + username + "]已被锁定, 如有疑问请联系管理员[" + CommonConst.CONTACT + "]";
        } else if (e instanceof DisabledException) {
            code = StatusConst.ACCOUNT_DISABLED;
            message = "账户[" + username + "]已被禁用, 如有疑问请联系管理员[" + CommonConst.CONTACT + "]";
        }
        httpServletResponse.getWriter().write(JSON.toJSONString(Result.failure().code(code).message(message)));
    }
}
