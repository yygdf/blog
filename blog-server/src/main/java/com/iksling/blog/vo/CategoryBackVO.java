package com.iksling.blog.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;

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
    @Max(message = "分类名最大长度", value = 50)
    @ApiModelProperty(name = "categoryName", value = "分类名", dataType = "String")
    private String categoryName;

    /**
     * 0未公开，1已公开，默认1
     */
    @ApiModelProperty(name = "publicFlag", value = "0未公开，1已公开，默认1", dataType = "Boolean")
    private Boolean publicFlag;

    /**
     * 0未隐藏，1已隐藏，默认0
     */
    @ApiModelProperty(name = "hiddenFlag", value = "0未隐藏，1已隐藏，默认0", dataType = "Boolean")
    private Boolean hiddenFlag;
}
