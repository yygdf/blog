package com.iksling.blog.dto;

import lombok.Data;

import java.util.Date;

@Data
public class UserOnlinesBackDTO {
    /**
     * 账号id
     */
    private Integer id;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 用户头像
     */
    private String avatar;

    /**
     * 用户昵称
     */
    private String nickname;

    /**
     * 用户名
     */
    private String username;

    /**
     * 登录时间
     */
    private Date loginTime;

    /**
     * 登录设备
     */
    private String loginDevice;

    /**
     * 登录方式
     */
    private Integer loginMethod;

    /**
     * 登录平台
     */
    private Boolean loginPlatform;

    /**
     * ip来源
     */
    private String ipSource;

    /**
     * ip地址
     */
    private String ipAddress;
}