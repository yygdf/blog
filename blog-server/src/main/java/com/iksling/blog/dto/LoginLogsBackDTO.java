package com.iksling.blog.dto;

import lombok.Data;

import java.util.Date;

@Data
public class LoginLogsBackDTO {
    /**
     * 日志id
     */
    private Integer id;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 登录时间
     */
    private Date loginTime;

    /**
     * 登录设备，默认空串
     */
    private String loginDevice;

    /**
     * 登录方式，1邮箱，2QQ，3微信，4手机号
     */
    private Integer loginMethod;

    /**
     * 登录平台，0前台，1后台
     */
    private Boolean loginPlatform;

    /**
     * 操作系统类型，默认空串
     */
    private String loginSystem;

    /**
     * 浏览器类型，默认空串
     */
    private String loginBrowser;

    /**
     * ip来源，默认空串
     */
    private String ipSource;

    /**
     * ip地址，默认空串
     */
    private String ipAddress;
}