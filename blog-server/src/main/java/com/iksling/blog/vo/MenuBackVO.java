package com.iksling.blog.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@ApiModel(description = "菜单后台VO")
public class MenuBackVO {
    /**
     * 菜单id
     */
    @ApiModelProperty(name = "id", value = "菜单id", dataType = "Integer")
    private Integer id;

    /**
     * 父菜单id
     */
    @ApiModelProperty(name = "parentId", value = "父菜单id", dataType = "Integer")
    private Integer parentId;

    /**
     * 菜单图标
     */
    @NotBlank(message = "菜单图标不能为空")
    @Max(message = "菜单图标最大长度", value = 50)
    @ApiModelProperty(name = "icon", value = "菜单图标", required = true, dataType = "String")
    private String icon;

    /**
     * 排序指标
     */
    @NotNull(message = "排序指标不能为空")
    @Min(message = "排序指标最小值", value = 1)
    @Max(message = "排序指标最大值", value = 127)
    @ApiModelProperty(name = "rank", value = "排序指标", required = true, dataType = "Integer")
    private Integer rank;

    /**
     * 菜单路径
     */
    @NotBlank(message = "菜单路径不能为空")
    @Max(message = "菜单路径最大长度", value = 50)
    @ApiModelProperty(name = "path", value = "菜单路径", required = true, dataType = "String")
    private String path;

    /**
     * 菜单名称
     */
    @NotBlank(message = "菜单名称不能为空")
    @Max(message = "菜单名称最大长度", value = 50)
    @ApiModelProperty(name = "name", value = "菜单名称", required = true, dataType = "String")
    private String name;

    /**
     * 菜单组件
     */
    @NotBlank(message = "菜单组件不能为空")
    @Max(message = "菜单组件最大长度", value = 50)
    @ApiModelProperty(name = "component", value = "菜单组件", required = true, dataType = "String")
    private String component;

    /**
     * 0未隐藏，1已隐藏
     */
    @NotNull(message = "隐藏标志不能为null")
    @ApiModelProperty(name = "hideFlag", value = "0未隐藏，1已隐藏", required = true, dataType = "Boolean")
    private Boolean hideFlag;

    /**
     * 0未隐藏，1已隐藏
     */
    @NotNull(message = "隐藏标志不能为null")
    @ApiModelProperty(name = "hiddenFlag", value = "0未隐藏，1已隐藏", required = true, dataType = "Boolean")
    private Boolean hiddenFlag;

    /**
     * 0未禁用，1已禁用
     */
    @NotNull(message = "禁用标志不能为null")
    @ApiModelProperty(name = "disabledFlag", value = "0未禁用，1已禁用", required = true, dataType = "Boolean")
    private Boolean disabledFlag;
}