package com.iksling.blog.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
     * 用户账号
     */
    private String username;

    /**
     * 登录类型
     */
    private Integer loginType;

    /**
     * 登录时间
     */
    private Date loginTime;

    /**
     * 登录设备
     */
    private String loginDevice;

    /**
     * 浏览器类型
     */
    private String browser;

    /**
     * 操作系统类型
     */
    private String operationSystem;

    /**
     * IP地址
     */
    private String ipAddress;

    /**
     * IP来源
     */
    private String ipSource;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}