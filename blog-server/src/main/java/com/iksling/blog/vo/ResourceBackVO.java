package com.iksling.blog.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Size;

@Data
@ApiModel(description = "资源后台VO")
public class ResourceBackVO {
    /**
     * id
     */
    @ApiModelProperty(name = "id", value = "资源id", dataType = "Integer")
    private Integer id;

    /**
     * 父id
     */
    @ApiModelProperty(name = "parentId", value = "父资源id", dataType = "Integer")
    private Integer parentId;

    /**
     * 资源路径
     */
    @Size(min = 1, max = 50, message = "'resourceUri':{'minlength':1,'maxlength':50}")
    @ApiModelProperty(name = "resourceUri", value = "资源路径", dataType = "String")
    private String resourceUri;

    /**
     * 资源名称
     */
    @Size(min = 1, max = 50, message = "'resourceName':{'minlength':1,'maxlength':50}")
    @ApiModelProperty(name = "resourceName", value = "资源名称", dataType = "String")
    private String resourceName;

    /**
     * 请求方式
     */
    @Size(min = 1, max = 50, message = "'resourceRequestMethod':{'minlength':1,'maxlength':50}")
    @ApiModelProperty(name = "resourceRequestMethod", value = "请求方式", dataType = "String")
    private String resourceRequestMethod;

    /**
     * 0未禁用, 1已禁用
     */
    @ApiModelProperty(name = "disabledFlag", value = "0未禁用, 1已禁用", dataType = "Boolean")
    private Boolean disabledFlag;

    /**
     * 0未匿名, 1已匿名
     */
    @ApiModelProperty(name = "anonymousFlag", value = "0未匿名, 1已匿名", dataType = "Boolean")
    private Boolean anonymousFlag;
}