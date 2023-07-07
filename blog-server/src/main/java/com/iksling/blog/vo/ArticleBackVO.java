package com.iksling.blog.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
@Builder
@ApiModel(description = "文章后台VO")
public class ArticleBackVO {
    /**
     * 文章id
     */
    @ApiModelProperty(name = "id", value = "文章id", dataType = "Integer")
    private Integer id;

    /**
     * 分类id
     */
    @ApiModelProperty(name = "categoryId", value = "分类id", dataType = "Integer")
    private Integer categoryId;

    /**
     * 文章标题
     */
    @NotBlank(message = "文章标题不能为空")
    @ApiModelProperty(name = "articleTitle", value = "文章标题", required = true, dataType = "String")
    private String articleTitle;

    /**
     * 文章封面
     */
    @ApiModelProperty(name = "articleCover", value = "文章封面", dataType = "String")
    private String articleCover;

    /**
     * 文章内容
     */
    @NotBlank(message = "文章内容不能为空")
    @ApiModelProperty(name = "articleContent", value = "文章内容", required = true, dataType = "String")
    private String articleContent;

    /**
     * 0未置顶，1已置顶
     */
    @ApiModelProperty(name = "isTop", value = "0未置顶，1已置顶", dataType = "Boolean")
    private Boolean isTop;

    /**
     * 0是草稿，1不是草稿
     */
    @ApiModelProperty(name = "isDraft", value = "0不是草稿，1是草稿", dataType = "Boolean")
    private Boolean isDraft;

    /**
     * 0未公开，1已公开
     */
    @ApiModelProperty(name = "isPublic", value = "0未公开，1已公开", dataType = "Boolean")
    private Boolean isPublic;

    /**
     * 0未隐藏，1已隐藏
     */
    @ApiModelProperty(name = "isHidden", value = "0未隐藏，1已隐藏", dataType = "Boolean")
    private Boolean isHidden;

    /**
     * 0不可评论，1可评论
     */
    @ApiModelProperty(name = "isCommentable", value = "0不可评论，1可评论", dataType = "Boolean")
    private Boolean isCommentable;

    /**
     * 标签id集合
     */
    @ApiModelProperty(name = "tagIdList", value = "标签id集合", dataType = "List<Integer>")
    private List<Integer> tagIdList;
}
