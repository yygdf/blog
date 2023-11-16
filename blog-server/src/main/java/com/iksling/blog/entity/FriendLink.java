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
 * @TableName tb_friend_link
 */
@TableName(value ="tb_friend_link")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FriendLink implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 友链地址
     */
    private String linkUrl;

    /**
     * 友链描述
     */
    private String linkDesc;

    /**
     * 友链图标
     */
    private String linkLogo;

    /**
     * 友链名称
     */
    private String linkName;

    /**
     * 0未删除, 1已删除, 默认0
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