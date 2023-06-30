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
 * @TableName tb_message
 */
@TableName(value ="tb_message")
@Data
public class Message implements Serializable {
    /**
     * 留言id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 用户昵称
     */
    private String nickname;

    /**
     * 用户头像
     */
    private String avatar;

    /**
     * 留言内容
     */
    private String messageContent;

    /**
     * 留言速度
     */
    private Boolean messageSpeed;

    /**
     * IP地址
     */
    private String ipAddress;

    /**
     * IP来源
     */
    private String ipSource;

    /**
     * 创建时间
     */
    private Date createTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}