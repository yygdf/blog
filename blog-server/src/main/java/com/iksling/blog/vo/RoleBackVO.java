package com.iksling.blog.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class RoleBackVO {
    /**
     * 角色id
     */
    @ApiModelProperty(name = "id", value = "角色id", dataType = "Integer")
    private Integer id;

    /**
     * 角色描述
     */
    @NotBlank(message = "角色描述不能为空")
    @Max(message = "角色描述最大长度", value = 50)
    @ApiModelProperty(name = "roleDesc", value = "角色描述", required = true, dataType = "String")
    private String roleDesc;

    /**
     * 角色名称
     */
    @NotBlank(message = "角色名称不能为空")
    @Max(message = "角色名称最大长度", value = 50)
    @ApiModelProperty(name = "roleName", value = "角色名称", required = true, dataType = "String")
    private String roleName;

    /**
     * 角色权重
     */
    @NotNull(message = "角色权重不能为空")
    @Min(message = "角色权重最小值", value = 1)
    @Max(message = "角色权重最大值", value = 1000)
    @ApiModelProperty(name = "roleWeight", value = "角色权重", required = true, dataType = "Integer")
    private Integer roleWeight;

    /**
     * 0未禁用，1已禁用
     */
    @ApiModelProperty(name = "disabledFlag", value = "0未禁用，1已禁用", dataType = "Boolean")
    private Boolean disabledFlag;
}