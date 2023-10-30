package com.iksling.blog.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@ApiModel(description = "标签后台VO")
public class TagBackVO {
    /**
     * 标签id
     */
    @ApiModelProperty(name = "id", value = "标签id", dataType = "Integer")
    private Integer id;

    /**
     * 标签名
     */
    @NotNull(message = "'tagName':'NotNull'")
    @Size(min = 1, max = 50, message = "'tagName':{'minlength':1,'maxlength':50}")
    @ApiModelProperty(name = "tagName", value = "标签名", required = true, dataType = "String")
    private String tagName;
}
