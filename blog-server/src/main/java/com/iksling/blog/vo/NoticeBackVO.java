package com.iksling.blog.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@ApiModel(description = "通知后台VO")
public class NoticeBackVO {
    /**
     * 通知类型
     */
    @NotNull(message = "{V0039}")
    @ApiModelProperty(name = "type", value = "通知类型", dataType = "Integer")
    private Integer type;

    /**
     * 收件人id
     */
    @NotNull(message = "{V0040}")
    @ApiModelProperty(name = "userId", value = "收件人id", dataType = "Integer")
    private Integer userId;

    /**
     * 通知标题
     */
    @NotNull(message = "{V0041}")
    @Size(min = 1, max = 50, message = "{V0042}")
    @ApiModelProperty(name = "noticeTitle", value = "通知标题", dataType = "String")
    private String noticeTitle;

    /**
     * 通知内容
     */
    @NotNull(message = "{V0043}")
    @Size(min = 1, message = "{V0044}")
    @ApiModelProperty(name = "noticeContent", value = "通知内容", dataType = "String")
    private String noticeContent;
}
