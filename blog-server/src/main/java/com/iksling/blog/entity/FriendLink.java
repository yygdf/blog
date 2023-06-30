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
 * @TableName tb_friend_link
 */
@TableName(value ="tb_friend_link")
@Data
public class FriendLink implements Serializable {
    /**
     * 友链id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 友链名称
     */
    private String linkName;

    /**
     * 友链地址
     */
    private String linkUrl;

    /**
     * 友链描述
     */
    private String linkDesc;

    /**
     * 友链logo
     */
    private String linkLogo;

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