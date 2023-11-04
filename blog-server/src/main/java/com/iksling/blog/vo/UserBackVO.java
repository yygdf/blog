package com.iksling.blog.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Size;

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
    @Size(min = 1, max = 50, message = "'username':{'minlength':1,'maxlength':50}")
    @ApiModelProperty(name = "username", value = "用户名", dataType = "String")
    private String username;

    /**
     * 用户介绍
     */
    @Size(min = 1, max = 50, message = "'intro':{'minlength':1,'maxlength':50}")
    @ApiModelProperty(name = "intro", value = "用户介绍", dataType = "String")
    private String intro;

    /**
     * 用户邮箱
     */
    @Size(min = 1, max = 50, message = "'email':{'minlength':1,'maxlength':50}")
    @ApiModelProperty(name = "email", value = "用户邮箱", dataType = "String")
    private String email;

    /**
     * 用户头像
     */
    @Size(max = 255, message = "'avatar':{'maxlength':255}")
    @ApiModelProperty(name = "avatar", value = "用户头像", dataType = "String")
    private String avatar;

    /**
     * 用户性别
     */
    @Range(min = 1, max = 4, message = "'gender':{'minvalue':1,'maxvalue':4}")
    @ApiModelProperty(name = "gender", value = "用户性别", dataType = "Integer")
    private Integer gender;

    /**
     * 用户网站
     */
    @Size(min = 1, max = 255, message = "'website':{'minlength':1,'maxlength':255}")
    @ApiModelProperty(name = "website", value = "用户网站", dataType = "String")
    private String website;

    /**
     * 用户昵称
     */
    @Size(min = 1, max = 50, message = "'nickname':{'minlength':1,'maxlength':50}")
    @ApiModelProperty(name = "nickname", value = "用户昵称", dataType = "String")
    private String nickname;
}