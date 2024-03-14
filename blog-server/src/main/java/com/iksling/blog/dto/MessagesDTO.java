package com.iksling.blog.dto;

import lombok.Data;

@Data
public class MessagesDTO {
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
     * 停留时间
     */
    private Integer time;

    /**
     * 留言内容
     */
    private String messageContent;
}