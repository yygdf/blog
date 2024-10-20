package com.iksling.blog.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@ApiModel(description = "QQ认证VO")
public class QQOauthVO {
    /**
     * openid
     */
    @NotNull(message = "{V0049}")
    @ApiModelProperty(name = "openid", value = "openid", dataType = "String")
    private String openid;

    /**
     * accessToken
     */
    @NotNull(message = "{V0050}")
    @ApiModelProperty(name = "accessToken", value = "accessToken", dataType = "String")
    private String accessToken;
}