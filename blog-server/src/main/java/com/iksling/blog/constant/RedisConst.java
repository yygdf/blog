package com.iksling.blog.constant;

public class RedisConst {
    /**
     * 用户点赞文章
     */
    public static final String ARTICLE_USER_LIKE = "article_user_like";

    /**
     * 用户点赞评论
     */
    public static final String COMMENT_USER_LIKE = "comment_user_like";

    /**
     * 博客访问量
     */
    public static final String BLOG_VIEW_COUNT = "blog_view_count";

    /**
     * 文章浏览量
     */
    public static final String ARTICLE_VIEW_COUNT = "article_view_count";

    /**
     * 文章点赞量
     */
    public static final String ARTICLE_LIKE_COUNT = "article_like_count";

    /**
     * 评论点赞量
     */
    public static final String COMMENT_LIKE_COUNT = "comment_like_count";

    /**
     * 用户非法操作前缀
     */
    public static final String USER_ILLEGAL_OPERATION = "user_illegal_operation";

    /**
     * 文件令牌前缀
     */
    public static final String MULTI_FILE_TOKEN = "multi_file_token";

    /**
     * 关于我
     */
    public static final String BLOG_ABOUT_ME = "blog_about_me";

    /**
     * 邮箱注册验证码前缀
     */
    public static final String EMAIL_REGISTER_CODE = "email_register_code";

    /**
     * 邮箱重设密码验证码前缀
     */
    public static final String EMAIL_FORGET_CODE = "email_forget_code";

    /**
     * 邮箱换绑验证码前缀
     */
    public static final String EMAIL_MODIFY_CODE = "email_modify_code";

    /**
     * 邮箱验证码过期时间
     */
    public static final Integer CODE_EXPIRE_TIME = 15 * 60 * 1000;

    /**
     * 文章令牌前缀
     */
    public static final String ARTICLE_TOKEN = "article_token";

    /**
     * 分类令牌前缀
     */
    public static final String CATEGORY_TOKEN = "category_token";

    /**
     * 用户消息提醒设置
     */
    public static String USER_MESSAGE_CONFIG = "user_message_config";

    /**
     * 登录令牌前缀
     */
    public static final String LOGIN_TOKEN = "login_token";

    /**
     * 登录令牌过期时间
     */
    public static final Integer TOKEN_EXPIRE_TIME = 60 * 60 * 1000;

    /**
     * 用户收藏音乐
     */
    public static final String MUSIC_USER_COLLECT = "music_user_collect";
}
