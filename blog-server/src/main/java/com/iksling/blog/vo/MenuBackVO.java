package com.iksling.blog.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Size;

@Data
@ApiModel(description = "菜单后台VO")
public class MenuBackVO {
    /**
     * id
     */
    @ApiModelProperty(name = "id", value = "菜单id", dataType = "Integer")
    private Integer id;

    /**
     * 父id
     */
    @ApiModelProperty(name = "parentId", value = "父菜单id", dataType = "Integer")
    private Integer parentId;

    /**
     * 菜单图标
     */
    @Size(min = 1, max = 50, message = "{V0019}")
    @ApiModelProperty(name = "icon", value = "菜单图标", dataType = "String")
    private String icon;

    /**
     * 排序指标
     */
    @Range(min = 1, max = 100, message = "{V0020}")
    @ApiModelProperty(name = "rank", value = "排序指标", dataType = "Integer")
    private Integer rank;

    /**
     * 菜单路径
     */
    @Size(min = 1, max = 50, message = "{V0021}")
    @ApiModelProperty(name = "path", value = "菜单路径", dataType = "String")
    private String path;

    /**
     * 菜单名称
     */
    @Size(min = 1, max = 50, message = "{V0022}")
    @ApiModelProperty(name = "name", value = "菜单名称", dataType = "String")
    private String name;

    /**
     * 菜单名称(en)
     */
    @Size(min = 1, max = 50, message = "{V0077}")
    @ApiModelProperty(name = "nameEn", value = "菜单名称(en)", dataType = "String")
    private String nameEn;

    /**
     * 菜单组件
     */
    @Size(min = 1, max = 50, message = "{V0023}")
    @ApiModelProperty(name = "component", value = "菜单组件", dataType = "String")
    private String component;

    /**
     * 0未隐藏, 1已隐藏
     */
    @ApiModelProperty(name = "hideFlag", value = "0未隐藏, 1已隐藏", dataType = "Boolean")
    private Boolean hideFlag;

    /**
     * 0未隐藏, 1已隐藏
     */
    @ApiModelProperty(name = "hiddenFlag", value = "0未隐藏, 1已隐藏", dataType = "Boolean")
    private Boolean hiddenFlag;

    /**
     * 0未禁用, 1已禁用
     */
    @ApiModelProperty(name = "disabledFlag", value = "0未禁用, 1已禁用", dataType = "Boolean")
    private Boolean disabledFlag;
}