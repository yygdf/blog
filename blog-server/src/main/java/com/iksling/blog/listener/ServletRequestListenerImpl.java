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

import static com.iksling.blog.constant.RedisConst.*;
import static com.iksling.blog.util.DateUtil.*;

@Component
public class ServletRequestListenerImpl implements ServletRequestListener {
    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public void requestInitialized(ServletRequestEvent sre) {
        HttpServletRequest request = (HttpServletRequest) sre.getServletRequest();
        HttpSession session = request.getSession();
        String ip = (String) session.getAttribute("ip");
        String ipAddress = IpUtil.getIpAddress(request);
        if (!ipAddress.equals(ip)) {
            session.setAttribute("ip", ipAddress);
            redisTemplate.boundValueOps(BLOG_VIEW_COUNT).increment(1);
        }
        redisTemplate.boundSetOps(dateToStr(new Date(), YYYY_MM_DD)).add(ipAddress);
    }

    @Scheduled(cron = " 0 0 0 * * ?")
    private void clear() {
        redisTemplate.delete(dateToStr(getSomeDay(new Date(), -7), YYYY_MM_DD));
    }
}
