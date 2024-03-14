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
    @NotNull(message = "'messageSpeed':'NotNull'")
    @Range(min = 1, max = 10, message = "'messageSpeed':{'minvalue':1,'maxvalue':10}")
    @ApiModelProperty(name = "messageSpeed", value = "留言速度", dataType = "Integer")
    private Integer messageSpeed;

    /**
     * 留言内容
     */
    @NotNull(message = "'messageContent':'NotNull'")
    @Size(min = 1, max = 100, message = "'messageContent':{'minlength':1,'maxlength':50}")
    @ApiModelProperty(name = "messageContent", value = "留言内容", dataType = "String")
    private String messageContent;
}