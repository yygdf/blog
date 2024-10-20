package com.iksling.blog.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class MessageVO {
    /**
     * 留言速度
     */
    @NotNull(message = "{V0024}")
    @Range(min = 1, max = 10, message = "{V0025}")
    @ApiModelProperty(name = "messageSpeed", value = "留言速度", dataType = "Integer")
    private Integer messageSpeed;

    /**
     * 留言内容
     */
    @NotNull(message = "{V0026}")
    @Size(min = 1, max = 100, message = "{V0027}")
    @ApiModelProperty(name = "messageContent", value = "留言内容", dataType = "String")
    private String messageContent;
}