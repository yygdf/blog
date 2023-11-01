package com.iksling.blog.dto;

import lombok.Data;

import java.util.Date;

@Data
public class FriendLinksBackDTO {
    /**
     * id
     */
    private Integer id;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 用户名
     */
    private String username;

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
     * 创建时间
     */
    private Date createTime;
}