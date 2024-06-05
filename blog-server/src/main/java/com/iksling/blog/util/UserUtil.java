package com.iksling.blog.util;

import com.iksling.blog.pojo.LoginUser;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.HashMap;

import static com.iksling.blog.constant.CommonConst.USER_MESSAGE_CONFIG_MAP;
import static com.iksling.blog.constant.RedisConst.USER_MESSAGE_CONFIG;

public class UserUtil {
    public static LoginUser getLoginUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return "anonymousUser".equals(principal) ? getAnonymousUser() : (LoginUser) principal;
    }

    public static LoginUser getAnonymousUser() {
        return LoginUser.builder()
                .userId(-1)
                .roleWeight(1000)
                .build();
    }

    public static HashMap<String, Integer> getUserMessageConfig(Integer userId) {
        return getUserMessageConfig(userId.toString());
    }

    public static HashMap<String, Integer> getUserMessageConfig(String userId) {
        HashMap<String, Integer> map = USER_MESSAGE_CONFIG_MAP.get(userId);
        if (map == null) {
            map = new HashMap<>();
            map.put("1", 1);
            map.put("2", 1);
            map.put("3", 1);
            map.put("4", 1);
            USER_MESSAGE_CONFIG_MAP.put(userId, map);
            RedisUtil.setMapValue(USER_MESSAGE_CONFIG, userId, map);
        }
        return map;
    }
}
