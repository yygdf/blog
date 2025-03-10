package com.iksling.blog.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@ApiModel(description = "用户后台VO")
public class EmailCodeVO {
    /**
     * 用户邮箱
     */
    @NotNull(message = "{V0009}")
    @Size(min = 1, max = 50, message = "{V0010}")
    @ApiModelProperty(name = "email", value = "用户邮箱", dataType = "String")
    private String email;

    /**
     * 类型
     */
    @ApiModelProperty(name = "type", value = "类型", dataType = "Integer")
    private Integer type;
}