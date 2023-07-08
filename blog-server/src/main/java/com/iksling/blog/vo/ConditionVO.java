package com.iksling.blog.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@ApiModel(description = "查询条件VO")
public class ConditionVO {
    /**
     * 每页数量
     */
    @NotBlank(message = "每页数量不能为空")
    @ApiModelProperty(name = "size", value = "每页数量", required = true, dataType = "Integer")
    private Integer size;

    /**
     * 当前页码
     */
    @NotBlank(message = "当前页码不能为空")
    @ApiModelProperty(name = "current", value = "当前页码", required = true, dataType = "Integer")
    private Integer current;

    /**
     * 搜索关键字
     */
    @ApiModelProperty(name = "keywords", value = "搜索关键字", required = true, dataType = "String")
    private String keywords;

    /**
     * 是否为草稿
     */
    @ApiModelProperty(name = "draftFlag", value = "是否为草稿", dataType = "Boolean")
    private Boolean draftFlag;

    /**
     * 是否为垃圾
     */
    @NotBlank(message = "是否为垃圾不能为空")
    @ApiModelProperty(name = "garbageFlag", value = "是否为垃圾", dataType = "Boolean")
    private Boolean garbageFlag;
}
