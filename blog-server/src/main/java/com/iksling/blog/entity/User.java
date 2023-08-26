package com.iksling.blog.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @TableName tb_user
 */
@TableName(value ="tb_user")
@Data
public class User implements Serializable {
    /**
     * 用户id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 用户介绍
     */
    private String intro;

    /**
     * 用户邮箱
     */
    private String email;

    /**
     * 用户头像
     */
    private String avatar;

    /**
     * 用户网站
     */
    private String website;

    /**
     * 用户昵称
     */
    private String nickname;

    /**
     * 0未删除，1已删除
     */
    private Boolean deletedFlag;

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