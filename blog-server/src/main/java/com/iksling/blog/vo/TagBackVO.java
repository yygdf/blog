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
     * id
     */
    @ApiModelProperty(name = "id", value = "标签id", dataType = "Integer")
    private Integer id;

    /**
     * 标签名称
     */
    @NotNull(message = "{V0063}")
    @Size(min = 1, max = 50, message = "{V0064}")
    @ApiModelProperty(name = "tagName", value = "标签名称", required = true, dataType = "String")
    private String tagName;
}
