package com.iksling.blog.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class ChatRecordsDTO implements Serializable {
    /**
     * id
     */
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
     * ip来源
     */
    private String ipSource;

    /**
     * ip地址
     */
    private String ipAddress;

    /**
     * 创建时间
     */
    private Date createTime;
}