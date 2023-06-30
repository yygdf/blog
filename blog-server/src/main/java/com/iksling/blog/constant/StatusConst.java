package com.iksling.blog.constant;

public class StatusConst {

    /**
     * 成功
     */
    public static final int SUCCESS = 20000;

    /**
     * 失败
     */
    public static final int FAILURE = 20001;

    /**
     * 认证失败
     */
    public static final int AUTHENTICATION_FAILURE = 40001;

    /**
     * 授权失败
     */
    public static final int AUTHORIZATION_FAILURE = 40002;

    /**
     * 未登录
     */
    public static final int NOT_LOGIN = 40003;

    /**
     * 会话过期
     */
    public static final int INVALID_SESSION = 40004;

    /**
     * 重复登录
     */
    public static final int EXPIRED_SESSION = 40005;

    /**
     * 非法请求
     */
    public static final int ILLEGAL_REQUEST = 50001;

    /**
     * 账户被锁定
     */
    public static final int ACCOUNT_LOCKED = 50002;
}
