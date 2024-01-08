package com.iksling.blog.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@ApiModel(description = "用户配置后台VO")
public class UserConfigBackVO {
    /**
     * id
     */
    @NotNull(message = "'id':'NotNull'")
    @ApiModelProperty(name = "id", value = "配置id", dataType = "Integer")
    private Integer id;

    /**
     * 配置描述
     */
    @Size(max = 255, message = "'configDesc':{'maxlength':255}")
    @ApiModelProperty(name = "configDesc", value = "配置描述", dataType = "String")
    private String configDesc;

    /**
     * 配置值
     */
    @Size(max = 255, message = "'configValue':{'maxlength':255}")
    @ApiModelProperty(name = "configValue", value = "配置值", dataType = "String")
    private String configValue;

    /**
     * 0未同步, 1已同步
     */
    @ApiModelProperty(name = "assimilateFlag", value = "0未同步, 1已同步", dataType = "Boolean")
    private Boolean assimilateFlag;
}