package com.iksling.blog.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@ApiModel(description = "配置状态VO")
public class ConfigStatusBackVO {
    /**
     * 用户id
     */
    @NotNull(message = "用户id不能为null")
    @ApiModelProperty(name = "userId", value = "用户id", required = true, dataType = "Integer")
    private Integer userId;

    /**
     * 配置名
     */
    @NotBlank(message = "配置名不能为空")
    @Max(message = "配置名最大长度", value = 50)
    @ApiModelProperty(name = "configName", value = "配置名", required = true, dataType = "String")
    private String configName;

    /**
     * 是否已删除
     */
    @NotNull(message = "删除标志不能为null")
    @ApiModelProperty(name = "deletedFlag", value = "是否已删除", required = true, dataType = "Boolean")
    private Boolean deletedFlag;
}
