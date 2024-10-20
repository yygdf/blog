package com.iksling.blog.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@ApiModel(description = "邮箱VO")
public class EmailVO {
    /**
     * 用户密码
     */
    @NotNull(message = "{V0011}")
    @Size(min = 6, max = 255, message = "{V0012}")
    @ApiModelProperty(name = "password", value = "用户密码", dataType = "String")
    private String password;

    /**
     * 用户邮箱
     */
    @NotNull(message = "{V0009}")
    @Size(min = 1, max = 50, message = "{V0010}")
    @ApiModelProperty(name = "email", value = "用户邮箱", dataType = "String")
    private String email;

    /**
     * 验证码
     */
    @NotNull(message = "{V0013}")
    @Size(min = 6, max = 6, message = "{V0014}")
    @ApiModelProperty(name = "code", value = "验证码", dataType = "String")
    private String code;
}