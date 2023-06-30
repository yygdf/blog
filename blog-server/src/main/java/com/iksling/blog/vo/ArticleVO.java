package com.iksling.blog.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "文章VO")
public class ArticleVO {
    /**
     * 文章id
     */
    @ApiModelProperty(name = "id", value = "文章id", dataType = "Integer")
    private Integer id;

    /**
     * 文章标题
     */
    @NotBlank(message = "文章标题不能为空")
    @ApiModelProperty(name = "articleTitle", value = "文章标题", required = true, dataType = "String")
    private String articleTitle;

    /**
     * 文章内容
     */
    @NotBlank(message = "文章内容不能为空")
    @ApiModelProperty(name = "articleContent", value = "文章内容", required = true, dataType = "String")
    private String articleContent;

    /**
     * 文章封面
     */
    @ApiModelProperty(name = "articleCover", value = "文章封面", dataType = "String")
    private String articleCover;

    /**
     * 分类id
     */
    @ApiModelProperty(name = "categoryId", value = "分类id", dataType = "Integer")
    private Integer categoryId;

    /**
     * 标签id集合
     */
    @ApiModelProperty(name = "tagIdList", value = "标签id集合", dataType = "List<Integer>")
    private List<Integer> tagIdList;

    /**
     * 0未置顶，1已置顶
     */
    @ApiModelProperty(name = "isTop", value = "0未置顶，1已置顶", dataType = "Integer")
    private Integer isTop;

    /**
     * 0是草稿，1不是草稿
     */
    @ApiModelProperty(name = "isDraft", value = "0是草稿，1不是草稿", dataType = "Integer")
    private Integer isDraft;
}
