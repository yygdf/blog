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
 * @TableName tb_chat_record
 */
@TableName(value ="tb_chat_record")
@Data
public class ChatRecord implements Serializable {
    /**
     * 聊天记录id
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
    private Boolean chatType;

    /**
     * 聊天内容
     */
    private String chatContent;

    /**
     * 0未撤回，1已撤回
     */
    private Boolean recalledFlag;

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

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}