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
 * @TableName tb_chat_record
 */
@TableName(value ="tb_chat_record")
@Data
public class ChatRecord implements Serializable {
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
     * 用户头像
     */
    private String avatar;

    /**
     * 用户昵称
     */
    private String nickname;

    /**
     * 聊天类型
     */
    private Integer chatType;

    /**
     * 聊天内容
     */
    private String chatContent;

    /**
     * 0未撤回, 1已撤回, 默认0
     */
    private Boolean recalledFlag;

    /**
     * ip来源, 默认空串
     */
    private String ipSource;

    /**
     * ip地址, 默认空串
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

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}