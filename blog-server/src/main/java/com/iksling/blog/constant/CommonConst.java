package com.iksling.blog.constant;

import java.util.Arrays;
import java.util.List;

public class CommonConst {
    /**
     * 网站管理员联系方式
     */
    public static final String ADMIN_CONTACT = "QQ:294513634";

    /**
     * 默认用户密码
     */
    public static final String DEFAULT_PASSWORD = "$2a$10$4DSTSm0GlWleNGt.eQ4mCuq6m4FiiNx9JJp0hl2SdEP5J5JtEjrHC";

    /**
     * 默认用户角色id
     */
    public static final Integer DEFAULT_ROLE_ID = 5;

    /**
     * 静态资源根路径
     */
    public static final String STATIC_RESOURCE_URL = "http://192.168.203.130/static/";

    /**
     * 核心用户id
     */
    public static final Integer ROOT_USER_ID = 2;

    /**
     * 核心用户idList
     */
    public static final List<Integer> ROOT_USER_ID_LIST = Arrays.asList(0, 1, 2);

    /**
     * 首页菜单id
     */
    public static final Integer HOME_MENU_ID = 1;

    /**
     * 核心角色id
     */
    public static final Integer ROOT_ROLE_ID = 1;

    /**
     * 核心角色idList
     */
    public static final List<Integer> ROOT_ROLE_ID_LIST = Arrays.asList(1);
}
