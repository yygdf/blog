package com.iksling.blog.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;

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
    @NotBlank(message = "配置描述不能为空")
    @Max(message = "配置描述最大长度", value = 255)
    @ApiModelProperty(name = "configDesc", value = "配置描述", required = true, dataType = "String")
    private String configDesc;

    /**
     * 配置值
     */
    @NotBlank(message = "配置值不能为空")
    @Max(message = "配置值最大长度", value = 255)
    @ApiModelProperty(name = "configValue", value = "配置值", required = true, dataType = "String")
    private String configValue;

    /**
     * 配置名
     */
    @NotBlank(message = "配置名不能为空")
    @Max(message = "配置名最大长度", value = 50)
    @ApiModelProperty(name = "configName", value = "配置名", required = true, dataType = "String")
    private String configName;
}