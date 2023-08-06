package com.iksling.blog.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@ApiModel(description = "分类后台VO")
public class CategoryBackVO {
    /**
     * 分类id
     */
    @ApiModelProperty(name = "id", value = "分类id", dataType = "Integer")
    private Integer id;

    /**
     * 分类名
     */
    @NotBlank(message = "分类名不能为空")
    @Max(message = "分类名最大长度", value = 50)
    @ApiModelProperty(name = "categoryName", value = "分类名", required = true, dataType = "String")
    private String categoryName;

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
}
