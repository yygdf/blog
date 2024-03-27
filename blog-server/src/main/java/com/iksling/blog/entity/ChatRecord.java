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
 * @TableName tb_chat_record
 */
@TableName(value ="tb_chat_record")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatRecord implements Serializable {
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
     * 用户头像, 默认空串
     */
    private String avatar;

    /**
     * 用户昵称, 默认空串
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