package com.iksling.blog.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Size;

@Data
@ApiModel(description = "用户VO")
public class UserVO {
    /**
     * 用户介绍
     */
    @Size(max = 50, message = "{V0070}")
    @ApiModelProperty(name = "intro", value = "用户介绍", dataType = "String")
    private String intro;

    /**
     * 用户性别
     */
    @Range(min = 1, max = 4, message = "{V0072}")
    @ApiModelProperty(name = "gender", value = "用户性别", dataType = "Integer")
    private Integer gender;

    /**
     * 用户网站
     */
    @Size(max = 255, message = "{V0073}")
    @ApiModelProperty(name = "website", value = "用户网站", dataType = "String")
    private String website;

    /**
     * 用户昵称
     */
    @Size(min = 1, max = 50, message = "{V0074}")
    @ApiModelProperty(name = "nickname", value = "用户昵称", dataType = "String")
    private String nickname;
}