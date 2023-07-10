package com.iksling.blog.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@ApiModel(description = "文章状态VO")
public class ArticleStatusVO {
    /**
     * 文章id
     */
    @NotNull(message = "文章id不能为null")
    @ApiModelProperty(name = "id", value = "文章id", dataType = "Integer")
    private Integer id;

    /**
     * 0未置顶，1已置顶
     */
    @NotNull(message = "置顶标志不能为null")
    @ApiModelProperty(name = "isTop", value = "0未置顶，1已置顶", dataType = "Boolean")
    private Boolean topFlag;

    /**
     * 0未公开，1已公开
     */
    @NotNull(message = "公开标志不能为null")
    @ApiModelProperty(name = "isPublic", value = "0未公开，1已公开", dataType = "Boolean")
    private Boolean publicFlag;

    /**
     * 0未隐藏，1已隐藏
     */
    @NotNull(message = "隐藏标志不能为null")
    @ApiModelProperty(name = "isHidden", value = "0未隐藏，1已隐藏", dataType = "Boolean")
    private Boolean hiddenFlag;

    /**
     * 0不可评论，1可评论
     */
    @NotNull(message = "评论标志不能为null")
    @ApiModelProperty(name = "isCommentable", value = "0不可评论，1可评论", dataType = "Boolean")
    private Boolean commentableFlag;
}
