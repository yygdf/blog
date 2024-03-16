package com.iksling.blog.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@ApiModel(description = "忘记密码VO")
public class PasswordForgetVO {
    /**
     * 用户密码
     */
    @NotNull(message = "'password':'NotNull'")
    @Size(min = 6, max = 255, message = "'password':{'minlength':6,'maxlength':255}")
    @ApiModelProperty(name = "password", value = "用户密码", dataType = "String")
    private String password;

    /**
     * 用户邮箱
     */
    @NotNull(message = "'email':'NotNull'")
    @Size(min = 1, max = 50, message = "'email':{'minlength':1,'maxlength':50}")
    @ApiModelProperty(name = "email", value = "用户邮箱", dataType = "String")
    private String email;

    /**
     * 验证码
     */
    @NotNull(message = "'code':'NotNull'")
    @Size(min = 6, max = 6, message = "'code':{'minlength':1,'maxlength':50}")
    @ApiModelProperty(name = "code", value = "验证码", dataType = "String")
    private String code;
}