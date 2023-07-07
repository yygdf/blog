package com.iksling.blog.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ArticleOptionDTO {
    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 文章标签列表
     */
    private List<TagDTO> tagDTOList;

    /**
     * 文章分类列表
     */
    private List<CategoryDTO> categoryDTOList;
}
