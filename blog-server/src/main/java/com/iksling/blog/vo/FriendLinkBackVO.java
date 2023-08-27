package com.iksling.blog.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;

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
    @NotBlank(message = "友链地址不能为空")
    @Max(message = "友链地址最大长度", value = 255)
    @ApiModelProperty(name = "linkUrl", value = "友链地址", required = true, dataType = "String")
    private String linkUrl;

    /**
     * 友链描述
     */
    @NotBlank(message = "友链描述不能为空")
    @Max(message = "友链描述最大长度", value = 50)
    @ApiModelProperty(name = "linkDesc", value = "友链描述", required = true, dataType = "String")
    private String linkDesc;

    /**
     * 友链图标
     */
    @NotBlank(message = "友链图标不能为空")
    @Max(message = "友链图标最大长度", value = 255)
    @ApiModelProperty(name = "linkLogo", value = "友链图标", required = true, dataType = "String")
    private String linkLogo;

    /**
     * 友链名称
     */
    @NotBlank(message = "友链名称不能为空")
    @Max(message = "友链名称最大长度", value = 50)
    @ApiModelProperty(name = "linkName", value = "友链名称", required = true, dataType = "String")
    private String linkName;
}