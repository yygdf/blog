package com.iksling.blog.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;

@Data
@ApiModel(description = "友链后台VO")
public class FriendLinkBackVO {
    /**
     * 友链id
     */
    @ApiModelProperty(name = "id", value = "友链id", dataType = "Integer")
    private Integer id;

    /**
     * 用户id
     */
    @ApiModelProperty(name = "userId", value = "用户id", dataType = "Integer")
    private Integer userId;

    /**
     * 友链地址
     */
    @Max(message = "友链地址最大长度", value = 255)
    @ApiModelProperty(name = "linkUrl", value = "友链地址", dataType = "String")
    private String linkUrl;

    /**
     * 友链描述
     */
    @Max(message = "友链描述最大长度", value = 50)
    @ApiModelProperty(name = "linkDesc", value = "友链描述", dataType = "String")
    private String linkDesc;

    /**
     * 友链图标
     */
    @Max(message = "友链图标最大长度", value = 255)
    @ApiModelProperty(name = "linkLogo", value = "友链图标", dataType = "String")
    private String linkLogo;

    /**
     * 友链名称
     */
    @Max(message = "友链名称最大长度", value = 50)
    @ApiModelProperty(name = "linkName", value = "友链名称", dataType = "String")
    private String linkName;
}