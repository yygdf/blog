package com.iksling.blog.listener;

import com.iksling.blog.util.IpUtil;
import com.iksling.blog.util.RedisUtil;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import static com.iksling.blog.constant.CommonConst.HOME_BLOGGER_ID;
import static com.iksling.blog.constant.RedisConst.BLOG_VIEW_COUNT;
import static com.iksling.blog.util.DateUtil.*;

@Component
public class ServletRequestListenerImpl implements ServletRequestListener {

    @Override
    public void requestInitialized(ServletRequestEvent sre) {
        HttpServletRequest request = (HttpServletRequest) sre.getServletRequest();
        String bloggerId = request.getParameter("bloggerId");
        if (bloggerId == null)
            bloggerId = HOME_BLOGGER_ID.toString();
        HttpSession session = request.getSession();
        Set<String> bloggerIdSet = (Set<String>) session.getAttribute("bloggerIdSet");
        if (bloggerIdSet == null)
            bloggerIdSet = new HashSet<>();
        if (!bloggerIdSet.contains(bloggerId)) {
            String ipAddress = IpUtil.getIpAddress(request);
            bloggerIdSet.add(bloggerId);
            session.setAttribute("bloggerIdSet", bloggerIdSet);
            RedisUtil.increment(BLOG_VIEW_COUNT, bloggerId, 1);
            RedisUtil.setSetValue(dateToStr(new Date(), YYYY_MM_DD), ipAddress + "_" + bloggerId);
        }
    }

    @Scheduled(cron = " 0 0 0 * * ?")
    private void clear() {
        RedisUtil.delKey(dateToStr(getSomeDay(new Date(), -7), YYYY_MM_DD));
    }
}
