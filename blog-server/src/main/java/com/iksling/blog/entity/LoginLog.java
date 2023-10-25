package com.iksling.blog.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @TableName tb_login_log
 */
@TableName(value ="tb_login_log")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginLog implements Serializable {
    /**
     * 日志id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 登录时间
     */
    private Date loginTime;

    /**
     * 登录设备, 默认空串
     */
    private String loginDevice;

    /**
     * 登录方式, 1邮箱, 2QQ, 3微信, 4手机号
     */
    private Integer loginMethod;

    /**
     * 登录平台, 0前台, 1后台
     */
    private Boolean loginPlatform;

    /**
     * 操作系统类型, 默认空串
     */
    private String loginSystem;

    /**
     * 浏览器类型, 默认空串
     */
    private String loginBrowser;

    /**
     * ip来源, 默认空串
     */
    private String ipSource;

    /**
     * ip地址, 默认空串
     */
    private String ipAddress;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}