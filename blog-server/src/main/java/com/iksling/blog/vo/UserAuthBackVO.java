package com.iksling.blog.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Size;
import java.util.List;

@Data
@ApiModel(description = "账号后台VO")
public class UserAuthBackVO {
    /**
     * 用户id
     */
    @ApiModelProperty(name = "id", value = "用户id", dataType = "Integer")
    private Integer id;

    /**
     * 用户密码
     */
    @Size(min = 1, max = 255, message = "'password':{'minlength':1,'maxlength':255}")
    @ApiModelProperty(name = "password", value = "用户密码", dataType = "String")
    private String password;

    /**
     * 0未锁定, 1已锁定
     */
    @ApiModelProperty(name = "lockedFlag", value = "0未锁定, 1已锁定", dataType = "Boolean")
    private Boolean lockedFlag;

    /**
     * 0未禁用, 1已禁用
     */
    @ApiModelProperty(name = "disabledFlag", value = "0未禁用, 1已禁用", dataType = "Boolean")
    private Boolean disabledFlag;

    /**
     * roleId列表
     */
    @Size(min = 1, message = "'roleIdList':{'min':1}")
    @ApiModelProperty(name = "roleIdList", value = "roleId列表", dataType = "List<Integer>")
    private List<Integer> roleIdList;
}