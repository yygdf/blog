package com.iksling.blog.constant;

import java.util.Arrays;
import java.util.List;

public class CommonConst {
    /**
     * 网站管理员联系方式
     */
    public static final String ADMIN_CONTACT = "QQ:294513634";

    /**
     * 网站管理员邮箱
     */
    public static final String ADMIN_EMAIL = "294513634@qq.com";

    /**
     * 默认用户头像
     */
    public static final String DEFAULT_AVATAR = "http://192.168.203.130/static/img/avatar/default/defaultAvatar.jpg";

    /**
     * 默认用户密码
     */
    public static final String DEFAULT_PASSWORD = "$2a$10$4aXL4Qc07EsoZxHQH60mHOJS6KAChpQrW5H.4d.7SLbHvBO0RdSoW";

    /**
     * 静态资源根路径
     */
    public static final String STATIC_RESOURCE_URL = "http://192.168.203.130/static/";

    /**
     * 系统核心用户id
     */
    public static final Integer ROOT_USER_ID = 2;

    /**
     * 系统核心用户账号idList
     */
    public static final List<Integer> ROOT_USER_AUTH_ID = Arrays.asList(0, 1, 2);
}
