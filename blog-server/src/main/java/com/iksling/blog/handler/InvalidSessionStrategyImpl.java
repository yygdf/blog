package com.iksling.blog.handler;

import com.alibaba.fastjson.JSON;
import com.iksling.blog.constant.StatusConst;
import com.iksling.blog.pojo.Result;
import org.springframework.security.web.session.InvalidSessionStrategy;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class InvalidSessionStrategyImpl implements InvalidSessionStrategy {
    @Override
    public void onInvalidSessionDetected(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {
        httpServletResponse.setContentType("application/json;charset=utf-8");
        Cookie cookie = new Cookie("JSESSIONID", "");
        cookie.setMaxAge(0);
        httpServletResponse.addCookie(cookie);
        httpServletResponse.getWriter().write(JSON.toJSONString(Result.failure().code(StatusConst.INVALID_SESSION).message("当前登录已失效, 请重新登录!")));
    }
}
