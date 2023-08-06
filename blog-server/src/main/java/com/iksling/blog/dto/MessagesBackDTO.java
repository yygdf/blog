package com.iksling.blog.dto;

import lombok.Data;

import java.util.Date;

@Data
public class MessagesBackDTO {
    /**
     * 留言id
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
     * 留言内容
     */
    private String messageContent;

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