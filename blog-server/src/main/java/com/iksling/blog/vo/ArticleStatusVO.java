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
    @ApiModelProperty(name = "id", value = "文章id", required = true, dataType = "Integer")
    private Integer id;

    /**
     * 0未置顶，1已置顶
     */
    @NotNull(message = "置顶标志不能为null")
    @ApiModelProperty(name = "topFlag", value = "0未置顶，1已置顶", required = true, dataType = "Boolean")
    private Boolean topFlag;

    /**
     * 0未公开，1已公开
     */
    @NotNull(message = "公开标志不能为null")
    @ApiModelProperty(name = "publicFlag", value = "0未公开，1已公开", required = true, dataType = "Boolean")
    private Boolean publicFlag;

    /**
     * 0未隐藏，1已隐藏
     */
    @NotNull(message = "隐藏标志不能为null")
    @ApiModelProperty(name = "hiddenFlag", value = "0未隐藏，1已隐藏", required = true, dataType = "Boolean")
    private Boolean hiddenFlag;

    /**
     * 0不可评论，1可评论
     */
    @NotNull(message = "评论标志不能为null")
    @ApiModelProperty(name = "commentableFlag", value = "0不可评论，1可评论", required = true, dataType = "Boolean")
    private Boolean commentableFlag;
}
