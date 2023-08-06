package com.iksling.blog.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@ApiModel(description = "通用状态VO")
public class CommonStatusVO {
    /**
     * id
     */
    @NotNull(message = "id不能为null")
    @ApiModelProperty(name = "id", value = "id", required = true, dataType = "Integer")
    private Integer id;

    /**
     * 0未置顶，1已置顶
     */
    @ApiModelProperty(name = "topFlag", value = "0未置顶，1已置顶", dataType = "Boolean")
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
    @ApiModelProperty(name = "commentableFlag", value = "0不可评论，1可评论", dataType = "Boolean")
    private Boolean commentableFlag;
}
