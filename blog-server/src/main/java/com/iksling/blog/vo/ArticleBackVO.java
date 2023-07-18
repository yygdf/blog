package com.iksling.blog.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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
    @Max(message = "文章标题最大长度", value = 50)
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
    @NotNull(message = "置顶标志不能为null")
    @ApiModelProperty(name = "isTop", value = "0未置顶，1已置顶", dataType = "Boolean")
    private Boolean topFlag;

    /**
     * 0是草稿，1不是草稿
     */
    @NotNull(message = "草稿标志不能为null")
    @ApiModelProperty(name = "draftFlag", value = "0不是草稿，1是草稿", dataType = "Boolean")
    private Boolean draftFlag;

    /**
     * 0未公开，1已公开
     */
    @NotNull(message = "公开标志不能为null")
    @ApiModelProperty(name = "publicFlag", value = "0未公开，1已公开", dataType = "Boolean")
    private Boolean publicFlag;

    /**
     * 0未隐藏，1已隐藏
     */
    @NotNull(message = "隐藏标志不能为null")
    @ApiModelProperty(name = "hiddenFlag", value = "0未隐藏，1已隐藏", dataType = "Boolean")
    private Boolean hiddenFlag;

    /**
     * 0不可评论，1可评论
     */
    @NotNull(message = "评论标志不能为null")
    @ApiModelProperty(name = "commentableFlag", value = "0不可评论，1可评论", dataType = "Boolean")
    private Boolean commentableFlag;

    /**
     * 标签id集合
     */
    @NotNull(message = "标签id集合不能为null")
    @ApiModelProperty(name = "tagIdList", value = "标签id集合", dataType = "List<Integer>")
    private List<Integer> tagIdList;
}
