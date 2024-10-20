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
    @NotNull(message = "{V0045}")
    @Size(min = 6, max = 255, message = "{V0046}")
    @ApiModelProperty(name = "oldPassword", value = "旧密码", dataType = "String")
    private String oldPassword;

    /**
     * 新密码
     */
    @NotNull(message = "{V0047}")
    @Size(min = 6, max = 255, message = "{V0048}")
    @ApiModelProperty(name = "newPassword", value = "新密码", dataType = "String")
    private String newPassword;
}