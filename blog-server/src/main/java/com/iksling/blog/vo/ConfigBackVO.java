package com.iksling.blog.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Size;

@Data
@ApiModel(description = "配置后台VO")
public class ConfigBackVO {
    /**
     * 配置id
     */
    @ApiModelProperty(name = "id", value = "配置id", dataType = "Integer")
    private Integer id;

    /**
     * 配置描述
     */
    @Size(min = 1, max = 255, message = "'configDesc':{'minlength':1,'maxlength':255}")
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
    @ApiModelProperty(name = "configName", value = "配置名", required = true, dataType = "String")
    private String configName;

    /**
     * 0不同步, 1需同步
     */
    @ApiModelProperty(name = "assimilateFlag", value = "0不同步, 1需同步", dataType = "Boolean")
    private Boolean assimilateFlag;
}