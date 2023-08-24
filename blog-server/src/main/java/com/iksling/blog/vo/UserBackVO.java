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
     * 用户介绍
     */
    @NotBlank(message = "用户介绍不能为空")
    @Max(message = "用户介绍最大长度", value = 50)
    private String intro;

    /**
     * 用户邮箱
     */
    @NotBlank(message = "用户邮箱不能为空")
    @Max(message = "用户邮箱最大长度", value = 50)
    private String email;

    /**
     * 用户头像
     */
    @NotBlank(message = "用户头像不能为空")
    @Max(message = "用户头像最大长度", value = 255)
    private String avatar;

    /**
     * 用户网站
     */
    @NotBlank(message = "用户网站不能为空")
    @Max(message = "用户网站最大长度", value = 255)
    private String website;

    /**
     * 用户昵称
     */
    @NotBlank(message = "用户昵称不能为空")
    @Max(message = "用户昵称最大长度", value = 50)
    private String nickname;
}