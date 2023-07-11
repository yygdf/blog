package com.iksling.blog.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@ApiModel(description = "分类状态VO")
public class CategoryStatusVO {
    /**
     * 分类id
     */
    @NotNull(message = "分类id不能为null")
    @ApiModelProperty(name = "id", value = "分类id", required = true, dataType = "Integer")
    private Integer id;

    /**
     * 0未公开，1已公开
     */
    @NotNull(message = "公开标志不能为null")
    @ApiModelProperty(name = "isPublic", value = "0未公开，1已公开", required = true, dataType = "Boolean")
    private Boolean publicFlag;

    /**
     * 0未隐藏，1已隐藏
     */
    @NotNull(message = "隐藏标志不能为null")
    @ApiModelProperty(name = "isHidden", value = "0未隐藏，1已隐藏", required = true, dataType = "Boolean")
    private Boolean hiddenFlag;
}
