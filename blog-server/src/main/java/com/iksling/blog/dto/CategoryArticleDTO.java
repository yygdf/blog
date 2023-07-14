package com.iksling.blog.dto;

import lombok.Data;

@Data
public class CategoryArticleDTO {
    /**
     * 分类id
     */
    private Integer categoryId;

    /**
     * 文章数
     */
    private Integer articleCount;
}
