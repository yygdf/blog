package com.iksling.blog.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName tb_qq_auth
 */
@TableName(value ="tb_qq_auth")
@Data
public class QQAuth implements Serializable {
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
     * QQ的openid
     */
    private String openid;

    /**
     * 访问QQ的token
     */
    private String accessToken;

    /**
     * 登录时间
     */
    private Date loginTime;

    /**
     * 登录设备
     */
    private String loginDevice;

    /**
     * 0未锁定，1已锁定
     */
    private Boolean lockedFlag;

    /**
     * 0未禁用，1已禁用
     */
    private Boolean disabledFlag;

    /**
     * ip来源
     */
    private String ipSource;

    /**
     * ip地址
     */
    private String ipAddress;

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