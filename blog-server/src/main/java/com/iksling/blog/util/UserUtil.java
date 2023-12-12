package com.iksling.blog.util;

import com.iksling.blog.pojo.LoginUser;
import org.springframework.security.core.context.SecurityContextHolder;

public class UserUtil {
    public static LoginUser getLoginUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return "anonymousUser".equals(principal) ? getAnonymousUser() : (LoginUser) principal;
    }

    public static LoginUser getAnonymousUser() {
        return LoginUser.builder()
                .userId(-1)
                .build();
    }
}
