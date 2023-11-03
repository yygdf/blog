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
public class ArticleOptionBackDTO {
    /**
     * 文章标签list
     */
    private List<LabelBackDTO> tagDTOList;

    /**
     * 文章分类list
     */
    private List<LabelBackDTO> categoryDTOList;

    /**
     * 静态资源根路径
     */
    private String staticResourceUrl;
}
