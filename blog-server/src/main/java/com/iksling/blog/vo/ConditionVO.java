package com.iksling.blog.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Builder
@ApiModel(description = "查询条件VO")
@NoArgsConstructor
@AllArgsConstructor
public class ConditionVO {
    /**
     * 每页数量
     */
    @NotNull(message = "每页数量不能为null")
    @ApiModelProperty(name = "size", value = "每页数量", required = true, dataType = "Integer")
    private Integer size;

    /**
     * 当前页码
     */
    @NotNull(message = "当前页码不能为null")
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
    @NotNull(message = "垃圾标志不能为null")
    @ApiModelProperty(name = "garbageFlag", value = "是否为垃圾", dataType = "Boolean")
    private Boolean garbageFlag;

    /**
     * 分类id
     */
    @ApiModelProperty(name = "categoryId", value = "分类id", dataType = "Integer")
    private Integer categoryId;

    /**
     * 标签id集合
     */
    @ApiModelProperty(name = "tagIdList", value = "标签id集合", dataType = "List<Integer>")
    private List<Integer> tagIdList;
}
