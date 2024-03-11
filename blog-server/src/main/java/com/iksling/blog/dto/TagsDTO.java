package com.iksling.blog.dto;

import lombok.Data;

@Data
public class TagsDTO {
    /**
     * id
     */
    private Integer id;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 标签名
     */
    private String tagName;
}