package com.iksling.blog.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "WS消息VO")
public class WebSocketMessageVO {
    /**
     * 类型
     */
    @ApiModelProperty(name = "type", value = "类型", dataType = "Integer")
    private Integer type;

    /**
     * 数据
     */
    @ApiModelProperty(name = "data", value = "数据", dataType = "Object")
    private Object data;
}