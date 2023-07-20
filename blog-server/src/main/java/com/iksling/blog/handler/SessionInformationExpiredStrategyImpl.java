package com.iksling.blog.handler;

import com.alibaba.fastjson.JSON;
import com.iksling.blog.pojo.Result;
import org.springframework.security.web.session.SessionInformationExpiredEvent;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.iksling.blog.constant.StatusConst.EXPIRED_SESSION;

@Component
public class SessionInformationExpiredStrategyImpl implements SessionInformationExpiredStrategy {
    @Override
    public void onExpiredSessionDetected(SessionInformationExpiredEvent sessionInformationExpiredEvent) throws IOException {
        HttpServletResponse httpServletResponse = sessionInformationExpiredEvent.getResponse();
        httpServletResponse.setContentType("application/json;charset=utf-8");
        httpServletResponse.getWriter().write(JSON.toJSONString(Result.failure().code(EXPIRED_SESSION).message("您的帐号已经在别的地方登录!如非本人操作请注意账号安全!")));
    }
}
