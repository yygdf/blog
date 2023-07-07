package com.iksling.blog.dto;

import lombok.Data;

@Data
public class TagDTO {
    /**
     * 标签id
     */
    private Integer id;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 标签名称
     */
    private String tagName;
}
