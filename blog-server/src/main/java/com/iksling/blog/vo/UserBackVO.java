package com.iksling.blog.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;

@Data
@ApiModel(description = "用户后台VO")
public class UserBackVO {
    /**
     * 用户id
     */
    @ApiModelProperty(name = "id", value = "用户id", dataType = "Integer")
    private Integer id;

    /**
     * 用户名
     */
    @Max(message = "用户名最大长度", value = 50)
    @ApiModelProperty(name = "username", value = "用户名", dataType = "String")
    private String username;

    /**
     * 用户介绍
     */
    @Max(message = "用户介绍最大长度", value = 50)
    @ApiModelProperty(name = "intro", value = "用户介绍", dataType = "String")
    private String intro;

    /**
     * 用户邮箱
     */
    @NotBlank(message = "用户邮箱不能为空")
    @Max(message = "用户邮箱最大长度", value = 50)
    @ApiModelProperty(name = "email", value = "用户邮箱", required = true, dataType = "String")
    private String email;

    /**
     * 用户头像
     */
    @Max(message = "用户头像最大长度", value = 255)
    @ApiModelProperty(name = "avatar", value = "用户头像", dataType = "String")
    private String avatar;

    /**
     * 用户网站
     */
    @Max(message = "用户网站最大长度", value = 255)
    @ApiModelProperty(name = "website", value = "用户网站", dataType = "String")
    private String website;

    /**
     * 用户昵称
     */
    @NotBlank(message = "用户昵称不能为空")
    @Max(message = "用户昵称最大长度", value = 50)
    @ApiModelProperty(name = "nickname", value = "用户昵称", required = true, dataType = "String")
    private String nickname;
}