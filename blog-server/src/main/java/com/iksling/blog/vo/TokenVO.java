package com.iksling.blog.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@ApiModel(description = "令牌后台VO")
public class TokenVO {
    /**
     * id
     */
    @NotNull(message = "{V0031}")
    @ApiModelProperty(name = "id", value = "id", dataType = "Integer")
    private Integer id;

    /**
     * 类型
     */
    @ApiModelProperty(name = "type", value = "类型", dataType = "Integer")
    private Integer type;

    /**
     * 访问密令
     */
    @NotNull(message = "{V0067}")
    @Size(min = 1, max = 100, message = "{V0065}")
    @ApiModelProperty(name = "accessToken", value = "访问密令", dataType = "String")
    private String accessToken;
}
