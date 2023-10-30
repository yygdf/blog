package com.iksling.blog.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
@ApiModel(description = "通用状态VO")
public class StatusBackVO {
    /**
     * id - 可删除
     */
    @ApiModelProperty(name = "id", value = "id", required = true, dataType = "Integer")
    private Integer id;

    /**
     * 0未置顶, 1已置顶 - 可删除
     */
    @ApiModelProperty(name = "topFlag", value = "0未置顶, 1已置顶", dataType = "Boolean")
    private Boolean topFlag;

    /**
     * 0未公开, 1已公开 - 可删除
     */
    @ApiModelProperty(name = "publicFlag", value = "0未公开, 1已公开", required = true, dataType = "Boolean")
    private Boolean publicFlag;

    /**
     * 0未隐藏, 1已隐藏 - 可删除
     */
    @ApiModelProperty(name = "hiddenFlag", value = "0未隐藏, 1已隐藏", dataType = "Boolean")
    private Boolean hiddenFlag;

    /**
     * 0不可评论, 1可评论 - 可删除
     */
    @ApiModelProperty(name = "commentableFlag", value = "0不可评论, 1可评论", dataType = "Boolean")
    private Boolean commentableFlag;

    /**
     * id列表
     */
    @NotEmpty(message = "'idList':{'min':1}")
    @ApiModelProperty(name = "idList", value = "id列表", required = true, dataType = "List<Integer>")
    private List<Integer> idList;

    /**
     * 类型
     */
    @ApiModelProperty(name = "type", value = "类型", dataType = "Integer")
    private Integer type;

    /**
     * 状态
     */
    @ApiModelProperty(name = "status", value = "状态", dataType = "Boolean")
    private Boolean status;
}
