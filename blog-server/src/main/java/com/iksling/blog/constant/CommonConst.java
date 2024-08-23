package com.iksling.blog.constant;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class CommonConst {
    /**
     * 网站管理员联系QQ
     */
    public static String ADMIN_CONTACT_QQ = "294513634";

    /**
     * 网站管理员联系邮箱
     */
    public static String ADMIN_CONTACT_EMAIL = "294513634@qq.com";

    /**
     * 网站前台地址
     */
    public static String WEBSITE_URL = "https://iksling.com";

    /**
     * 网站后台地址
     */
    public static String WEBSITE_URL_BACK = "https://www.iksling.com";

    /**
     * 默认用户密码
     */
    public static final String DEFAULT_PASSWORD = "$2a$10$4DSTSm0GlWleNGt.eQ4mCuq6m4FiiNx9JJp0hl2SdEP5J5JtEjrHC";

    /**
     * 默认用户角色id
     */
    public static Integer DEFAULT_ROLE_ID = 5;

    /**
     * 核心用户id
     */
    public static Integer ROOT_USER_ID = 2;

    /**
     * 核心用户id组
     */
    public static List<Integer> ROOT_USER_ID_LIST = Arrays.asList(0, 1, 2);

    /**
     * 后台首页菜单id
     */
    public static Integer HOME_MENU_ID = 1;

    /**
     * 核心角色id
     */
    public static Integer ROOT_ROLE_ID = 1;

    /**
     * 核心角色id组
     */
    public static List<Integer> ROOT_ROLE_ID_LIST = Collections.singletonList(1);

    /**
     * 默认角色同步标志
     */
    public static Boolean DEFAULT_ROLE_ASSIMILATE = false;

    /**
     * 前台首页博主id
     */
    public static Integer HOME_BLOGGER_ID = 2;

    /**
     * 是否启用用户配置
     */
    public static Boolean ENABLE_USER_CONFIG = true;

    /**
     * 检查文件类型是否启用严格模式
     */
    public static Boolean ENABLE_FILE_TYPE_STRICT = false;

    /**
     * 用户消息提醒设置
     */
    public static HashMap<String, HashMap<String, Integer>> USER_MESSAGE_CONFIG_MAP = new HashMap<>();
}
