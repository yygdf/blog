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
 * @TableName tb_message
 */
@TableName(value ="tb_message")
@Data
public class Message implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 用户id, 默认-1
     */
    private Integer userId;

    /**
     * 留言速度
     */
    private Integer messageSpeed;

    /**
     * 留言内容
     */
    private String messageContent;

    /**
     * 0未删除, 1已删除, 默认0
     */
    private Boolean deletedFlag;

    /**
     * ip来源, 默认空串
     */
    private String ipSource;

    /**
     * ip地址, 默认空串
     */
    private String ipAddress;

    /**
     * 创建人, 默认-1
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