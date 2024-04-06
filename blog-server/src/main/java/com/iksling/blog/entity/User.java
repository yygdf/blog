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
 * @TableName tb_user
 */
@TableName(value ="tb_user")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 用户介绍, 默认空串
     */
    private String intro;

    /**
     * 用户邮箱
     */
    private String email;

    /**
     * 用户头像, 默认空串
     */
    private String avatar;

    /**
     * 用户性别, 默认-1
     */
    private Integer gender;

    /**
     * 用户网站, 默认空串
     */
    private String website;

    /**
     * 用户昵称
     */
    private String nickname;

    /**
     * 0未修改，1已修改，默认0
     */
    private Boolean modifiedFlag;

    /**
     * 创建人, 默认null
     */
    private Integer createUser;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新人, 默认null
     */
    private Integer updateUser;

    /**
     * 更新时间, 默认null
     */
    private Date updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}