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
    @NotNull(message = "'openid':'NotNull'")
    @ApiModelProperty(name = "openid", value = "openid", dataType = "String")
    private String openid;

    /**
     * accessToken
     */
    @NotNull(message = "'accessToken':'NotNull'")
    @ApiModelProperty(name = "accessToken", value = "accessToken", dataType = "String")
    private String accessToken;
}