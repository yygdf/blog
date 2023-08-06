package com.iksling.blog.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;

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
    @NotBlank(message = "标签名不能为空")
    @Max(message = "标签名最大长度", value = 50)
    @ApiModelProperty(name = "tagName", value = "标签名", required = true, dataType = "String")
    private String tagName;
}
