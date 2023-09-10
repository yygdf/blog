package com.iksling.blog.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
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
    @Max(message = "用户密码最大长度", value = 255)
    @ApiModelProperty(name = "password", value = "用户密码", dataType = "String")
    private String password;

    /**
     * 0未锁定，1已锁定
     */
    @NotNull(message = "锁定标志不能为null")
    @ApiModelProperty(name = "lockedFlag", value = "0未锁定，1已锁定", required = true, dataType = "Boolean")
    private Boolean lockedFlag;

    /**
     * 0未禁用，1已禁用
     */
    @NotNull(message = "禁用标志不能为null")
    @ApiModelProperty(name = "disabledFlag", value = "0未禁用，1已禁用", required = true, dataType = "Boolean")
    private Boolean disabledFlag;

    /**
     * roleId列表
     */
    @NotEmpty(message = "roleId列表不能为空")
    @ApiModelProperty(name = "roleIdList", value = "roleId列表", required = true, dataType = "List<Integer>")
    private List<Integer> roleIdList;
}