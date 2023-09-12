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
     * 登录日志id
     */
    private Integer loginLogId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 用户密码
     */
    private String password;

    /**
     * 1邮箱，2QQ，3微信，4手机号
     */
    private Integer loginMethod;

    /**
     * 0未锁定，1已锁定
     */
    private Boolean lockedFlag;

    /**
     * 0未删除，1已删除
     */
    private Boolean deletedFlag;

    /**
     * 0未禁用，1已禁用
     */
    private Boolean disabledFlag;

    /**
     * 0未启用过，1已启用过
     */
    private Boolean userConfigFlag;

    /**
     * 创建人
     */
    private Integer createUser;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新人
     */
    private Integer updateUser;

    /**
     * 更新时间
     */
    private Date updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}