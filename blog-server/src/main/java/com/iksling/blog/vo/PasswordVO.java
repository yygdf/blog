package com.iksling.blog.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@ApiModel(description = "密码VO")
public class PasswordVO {
    /**
     * 旧密码
     */
    @NotNull(message = "'oldPassword':'NotNull'")
    @Size(min = 6, max = 255, message = "'oldPassword':{'minlength':6,'maxlength':255}")
    @ApiModelProperty(name = "oldPassword", value = "旧密码", dataType = "String")
    private String oldPassword;

    /**
     * 新密码
     */
    @NotNull(message = "'newPassword':'NotNull'")
    @Size(min = 6, max = 255, message = "'newPassword':{'minlength':6,'maxlength':255}")
    @ApiModelProperty(name = "newPassword", value = "新密码", dataType = "String")
    private String newPassword;
}