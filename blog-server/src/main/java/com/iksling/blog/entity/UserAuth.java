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
 * @TableName tb_user_auth
 */
@TableName(value ="tb_user_auth")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserAuth implements Serializable {
    /**
     * 账号id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 用户名称
     */
    private String username;

    /**
     * 用户密码
     */
    private String password;

    /**
     * 登录时间
     */
    private Date loginTime;

    /**
     * 登录设备
     */
    private String loginDevice;

    /**
     * IP地址
     */
    private String ipAddress;

    /**
     * IP来源
     */
    private String ipSource;

    /**
     * 0未锁定，1已锁定
     */
    private Integer isLocked;

    /**
     * 0未禁用，1已禁用
     */
    private Integer isDisabled;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}