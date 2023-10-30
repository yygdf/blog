package com.iksling.blog.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@ApiModel(description = "用户VO")
public class UserVO {
    /**
     * 用户介绍
     */
    @Size(min = 1, max = 50, message = "'intro':{'minlength':1,'maxlength':50}")
    @ApiModelProperty(name = "intro", value = "用户介绍", dataType = "String")
    private String intro;

    /**
     * 用户网站
     */
    @Size(min = 1, max = 255, message = "'website':{'minlength':1,'maxlength':255}")
    @ApiModelProperty(name = "website", value = "用户网站", dataType = "String")
    private String website;

    /**
     * 用户昵称
     */
    @NotNull(message = "'nickname':'NotNull'")
    @Size(min = 1, max = 50, message = "'nickname':{'minlength':1,'maxlength':50}")
    @ApiModelProperty(name = "nickname", value = "用户昵称", required = true, dataType = "String")
    private String nickname;
}