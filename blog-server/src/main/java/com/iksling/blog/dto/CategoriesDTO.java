package com.iksling.blog.dto;

import lombok.Data;

@Data
public class CategoriesDTO {
    /**
     * id
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

    /**
     * 0未公开, 1已公开
     */
    private Boolean publicFlag;

    /**
     * 0未隐藏, 1已隐藏
     */
    private Boolean hiddenFlag;

    /**
     * 文章数量
     */
    private Integer articleCount;
}