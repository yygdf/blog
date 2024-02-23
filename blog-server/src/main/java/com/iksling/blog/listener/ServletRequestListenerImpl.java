package com.iksling.blog.listener;

import com.iksling.blog.util.IpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;

import static com.iksling.blog.constant.CommonConst.ROOT_USER_ID;
import static com.iksling.blog.constant.RedisConst.BLOG_VIEW_COUNT;
import static com.iksling.blog.util.DateUtil.*;

@Component
public class ServletRequestListenerImpl implements ServletRequestListener {
    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public void requestInitialized(ServletRequestEvent sre) {
        HttpServletRequest request = (HttpServletRequest) sre.getServletRequest();
        String bloggerId = request.getParameter("bloggerId");
        if (bloggerId != null) {
            if (bloggerId.equals("-1"))
                bloggerId = ROOT_USER_ID.toString();
            HttpSession session = request.getSession();
            if (session.getAttribute(bloggerId) == null) {
                String ipAddress = IpUtil.getIpAddress(request);
                session.setAttribute(bloggerId, "");
                redisTemplate.boundValueOps(BLOG_VIEW_COUNT + "_" + bloggerId).increment(1);
                redisTemplate.boundSetOps(dateToStr(new Date(), YYYY_MM_DD)).add(ipAddress + "-" + bloggerId);
            }
        }
    }

    @Scheduled(cron = " 0 0 0 * * ?")
    private void clear() {
        redisTemplate.delete(dateToStr(getSomeDay(new Date(), -7), YYYY_MM_DD));
    }
}
