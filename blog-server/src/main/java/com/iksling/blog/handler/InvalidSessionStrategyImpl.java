package com.iksling.blog.handler;

import com.alibaba.fastjson.JSON;
import com.iksling.blog.pojo.Result;
import org.springframework.security.web.session.InvalidSessionStrategy;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.iksling.blog.constant.StatusConst.INVALID_SESSION;

@Component
public class InvalidSessionStrategyImpl implements InvalidSessionStrategy {

    @Override
    public void onInvalidSessionDetected(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException, ServletException {
        httpServletResponse.setContentType("application/json;charset=utf-8");
        if (httpServletRequest.getRequestURI().startsWith("/back"))
            httpServletResponse.getWriter().write(JSON.toJSONString(Result.failure().code(INVALID_SESSION).message("当前登录已失效, 请重新登录!")));
        else
            httpServletRequest.getRequestDispatcher(httpServletRequest.getRequestURI()).forward(httpServletRequest, httpServletResponse);
    }
}
