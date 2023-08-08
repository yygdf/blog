package com.iksling.blog.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@ApiModel(description = "资源后台VO")
public class ResourceBackVO {
    /**
     * 资源id
     */
    @ApiModelProperty(name = "id", value = "资源id", dataType = "Integer")
    private Integer id;

    /**
     * 父资源id
     */
    @ApiModelProperty(name = "parentId", value = "父资源id", dataType = "Integer")
    private Integer parentId;

    /**
     * 资源路径
     */
    @Max(message = "资源路径最大长度", value = 50)
    @ApiModelProperty(name = "resourceUri", value = "资源路径", required = true, dataType = "String")
    private String resourceUri;

    /**
     * 资源名称
     */
    @NotBlank(message = "资源名称不能为空")
    @Max(message = "资源名称最大长度", value = 50)
    @ApiModelProperty(name = "resourceName", value = "资源名称", required = true, dataType = "String")
    private String resourceName;

    /**
     * 请求方式
     */
    @Max(message = "请求方式最大长度", value = 50)
    @ApiModelProperty(name = "resourceRequestMethod", value = "请求方式", required = true, dataType = "String")
    private String resourceRequestMethod;

    /**
     * 0未禁用，1已禁用
     */
    @NotNull(message = "禁用标志不能为null")
    @ApiModelProperty(name = "disabledFlag", value = "0未禁用，1已禁用", required = true, dataType = "Boolean")
    private Boolean disabledFlag;

    /**
     * 0未匿名，1已匿名
     */
    @NotNull(message = "匿名标志不能为null")
    @ApiModelProperty(name = "anonymousFlag", value = "0未匿名，1已匿名", required = true, dataType = "Boolean")
    private Boolean anonymousFlag;
}