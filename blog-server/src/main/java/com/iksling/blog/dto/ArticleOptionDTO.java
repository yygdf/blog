package com.iksling.blog.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticleOptionDTO {
    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 文章标签集合
     */
    private List<LabelDTO> tagDTOList;

    /**
     * 文章分类集合
     */
    private List<LabelDTO> categoryDTOList;
}
