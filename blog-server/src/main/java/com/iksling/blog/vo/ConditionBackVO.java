package com.iksling.blog.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import java.util.Date;
import java.util.List;

@Data
@ApiModel(description = "查询条件")
public class ConditionBackVO {
    /**
     * 每页数量
     */
    @Min(value = 1, message = "'size':{'minvalue':1}")
    @ApiModelProperty(name = "size", value = "每页数量", dataType = "Integer")
    private Integer size = 10;

    /**
     * 当前页码
     */
    @Min(value = 1, message = "'current':{'minvalue':1}")
    @ApiModelProperty(name = "current", value = "当前页码", dataType = "Integer")
    private Integer current = 1;

    /**
     * 搜索关键字
     */
    @ApiModelProperty(name = "keywords", value = "搜索关键字", dataType = "String")
    private String keywords;

    /**
     * 用户id
     */
    @ApiModelProperty(name = "userId", value = "用户id", dataType = "Integer")
    private Integer userId;

    /**
     * 是否是草稿 - 可删除
     */
    @ApiModelProperty(name = "draftFlag", value = "是否是草稿", dataType = "Boolean")
    private Boolean draftFlag;

    /**
     * 是否已回收 - 可删除
     */
    @ApiModelProperty(name = "recycleFlag", value = "是否已回收", dataType = "Boolean")
    private Boolean recycleFlag;

    /**
     * 是否已删除 - 可删除
     */
    @ApiModelProperty(name = "deletedFlag", value = "是否已删除", dataType = "Boolean")
    private Boolean deletedFlag;

    /**
     * 分类id
     */
    @ApiModelProperty(name = "categoryId", value = "分类id", dataType = "Integer")
    private Integer categoryId;

    /**
     * 标签idList
     */
    @ApiModelProperty(name = "tagIdList", value = "标签idList", dataType = "List<Integer>")
    private List<Integer> tagIdList;

    /**
     * 类型
     */
    @ApiModelProperty(name = "type", value = "类型", dataType = "Integer")
    private Integer type;

    /**
     * 标志
     */
    @ApiModelProperty(name = "flag", value = "标志", dataType = "Boolean")
    private Boolean flag;

    /**
     * 起始时间
     */
    @ApiModelProperty(name = "startTime", value = "起始时间", dataType = "Date")
    private Date startTime;

    /**
     * 结束时间
     */
    @ApiModelProperty(name = "endTime", value = "结束时间", dataType = "Date")
    private Date endTime;
}
