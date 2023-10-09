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
    public static final String DEFAULT_PASSWORD = "$2a$10$4DSTSm0GlWleNGt.eQ4mCuq6m4FiiNx9JJp0hl2SdEP5J5JtEjrHC";

    /**
     * 静态资源根路径
     */
    public static final String STATIC_RESOURCE_URL = "http://192.168.203.130/static/";

    /**
     * 系统核心用户id
     */
    public static final Integer ROOT_USER_ID = 2;

    /**
     * 系统核心用户idList
     */
    public static final List<Integer> ROOT_USER_ID_LIST = Arrays.asList(0, 1, 2);

    /**
     * 系统首页菜单id
     */
    public static final Integer HOME_MENU_ID = 1;

    /**
     * 系统核心角色id
     */
    public static final Integer ROOT_ROLE_ID = 1;

    /**
     * 系统核心角色idList
     */
    public static final List<Integer> ROOT_ROLE_ID_LIST = Arrays.asList(1);

    /**
     * 聊天室音频目录路径
     */
    public static final String AUDIO_CHAT_PATH = "audio/chat";

    /**
     * 聊天室音频目录大小
     */
    public static final Integer AUDIO_CHAT_SIZE = 1;

    /**
     * 聊天室音频目录单位
     */
    public static final String AUDIO_CHAT_UNIT= "MB";

    /**
     * 用户头像目录路径
     */
    public static final String IMG_AVATAR_PATH = "img/avatar";

    /**
     * 用户头像目录大小
     */
    public static final Integer IMG_AVATAR_SIZE = 1;

    /**
     * 用户头像目录单位
     */
    public static final String IMG_AVATAR_UNIT= "MB";

    /**
     * 文章图片目录路径
     */
    public static final String IMG_ARTICLE_PATH = "img/article";

    /**
     * 文章图片目录大小
     */
    public static final Integer IMG_ARTICLE_SIZE = 5;

    /**
     * 文章图片目录单位
     */
    public static final String IMG_ARTICLE_UNIT= "MB";
}
