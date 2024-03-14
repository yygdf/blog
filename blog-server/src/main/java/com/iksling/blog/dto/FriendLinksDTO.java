package com.iksling.blog.dto;

import lombok.Data;

@Data
public class FriendLinksDTO {
    /**
     * id
     */
    private Integer id;

    /**
     * 用户id
     */
    private Integer userId;

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
}