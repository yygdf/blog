package com.iksling.blog.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import java.util.List;

@Data
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
    @Max(message = "文章标题最大长度", value = 50)
    @ApiModelProperty(name = "articleTitle", value = "文章标题", dataType = "String")
    private String articleTitle;

    /**
     * 文章封面
     */
    @Max(message = "文章封面最大长度", value = 255)
    @ApiModelProperty(name = "articleCover", value = "文章封面", dataType = "String")
    private String articleCover;

    /**
     * 文章内容
     */
    @ApiModelProperty(name = "articleContent", value = "文章内容", dataType = "String")
    private String articleContent;

    /**
     * 0未置顶, 1已置顶
     */
    @ApiModelProperty(name = "topFlag", value = "0未置顶, 1已置顶", dataType = "Boolean")
    private Boolean topFlag;

    /**
     * 0不是草稿, 1是草稿
     */
    @ApiModelProperty(name = "draftFlag", value = "0不是草稿, 1是草稿", dataType = "Boolean")
    private Boolean draftFlag;

    /**
     * 0未公开, 1已公开
     */
    @ApiModelProperty(name = "publicFlag", value = "0未公开, 1已公开", dataType = "Boolean")
    private Boolean publicFlag;

    /**
     * 0未隐藏, 1已隐藏
     */
    @ApiModelProperty(name = "hiddenFlag", value = "0未隐藏, 1已隐藏", dataType = "Boolean")
    private Boolean hiddenFlag;

    /**
     * 0不可评论, 1可评论
     */
    @ApiModelProperty(name = "commentableFlag", value = "0不可评论, 1可评论", dataType = "Boolean")
    private Boolean commentableFlag;

    /**
     * 标签idList
     */
    @ApiModelProperty(name = "tagIdList", value = "标签idList", dataType = "List<Integer>")
    private List<Integer> tagIdList;
}
