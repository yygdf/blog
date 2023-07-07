package com.iksling.blog.dto;

import lombok.Data;

@Data
public class CategoryDTO {
    /**
     * 分类id
     */
    private Integer id;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 分类名
     */
    private String categoryName;
}
