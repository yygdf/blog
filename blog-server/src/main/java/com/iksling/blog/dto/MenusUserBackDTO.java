package com.iksling.blog.dto;

import lombok.Data;

import java.util.List;

@Data
public class MenusUserBackDTO {
    /**
     * 菜单id
     */
    private Integer id;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 菜单图标
     */
    private String icon;

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
     * 子菜单
     */
    private List<MenusUserBackDTO> children;
}