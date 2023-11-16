package com.iksling.blog.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Size;

@Data
public class RoleBackVO {
    /**
     * id
     */
    @ApiModelProperty(name = "id", value = "角色id", dataType = "Integer")
    private Integer id;

    /**
     * 角色描述
     */
    @Size(min = 1, max = 50, message = "'roleDesc':{'minlength':1,'maxlength':50}")
    @ApiModelProperty(name = "roleDesc", value = "角色描述", dataType = "String")
    private String roleDesc;

    /**
     * 角色名称
     */
    @Size(min = 1, max = 50, message = "'roleName':{'minlength':1,'maxlength':50}")
    @ApiModelProperty(name = "roleName", value = "角色名称", dataType = "String")
    private String roleName;

    /**
     * 角色权重
     */
    @Range(min = 1, max = 1000, message = "'roleWeight':{'minvalue':1,'maxvalue':1000}")
    @ApiModelProperty(name = "roleWeight", value = "角色权重", dataType = "Integer")
    private Integer roleWeight;

    /**
     * 0未禁用, 1已禁用
     */
    @ApiModelProperty(name = "disabledFlag", value = "0未禁用, 1已禁用", dataType = "Boolean")
    private Boolean disabledFlag;
}