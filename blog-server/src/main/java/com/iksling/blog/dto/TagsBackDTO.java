package com.iksling.blog.dto;

import lombok.Data;

import java.util.Date;

@Data
public class TagsBackDTO {
    /**
     * 标签id
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
     * 标签名
     */
    private String tagName;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;
}