package com.iksling.blog.dto;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class MenusBackDTO {
    /**
     * id
     */
    private Integer id;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 父菜单id
     */
    private Integer parentId;

    /**
     * 菜单图标
     */
    private String icon;

    /**
     * 排序指标
     */
    private Integer rank;

    /**
     * 菜单路径
     */
    private String path;

    /**
     * 菜单名称
     */
    private String name;

    /**
     * 菜单组件
     */
    private String component;

    /**
     * 0未隐藏, 1已隐藏
     */
    private Boolean hideFlag;

    /**
     * 0未隐藏, 1已隐藏
     */
    private Boolean hiddenFlag;

    /**
     * 0未禁用, 1已禁用
     */
    private Boolean disabledFlag;

    /**
     * 0不可删除, 1可删除
     */
    private Boolean deletableFlag;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 子菜单
     */
    private List<MenusBackDTO> children;
}