package com.iksling.blog.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Size;

@Data
@ApiModel(description = "系统配置后台VO")
public class SystemConfigBackVO {
    /**
     * id
     */
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
    @Size(min = 1, max = 255, message = "'configValue':{'minlength':1,'maxlength':255}")
    @ApiModelProperty(name = "configValue", value = "配置值", dataType = "String")
    private String configValue;

    /**
     * 配置名
     */
    @Size(min = 1, max = 50, message = "'configName':{'minlength':1,'maxlength':50}")
    @ApiModelProperty(name = "configName", value = "配置名", dataType = "String")
    private String configName;
}