package com.iksling.blog.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import static com.iksling.blog.constant.CommonConst.STATIC_RESOURCE_URL;

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

    /**
     * 静态资源根路径
     */
    private String staticResourceUrl;
}
