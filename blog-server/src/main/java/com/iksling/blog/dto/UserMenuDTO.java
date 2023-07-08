package com.iksling.blog.dto;

import lombok.Data;

import java.util.List;

@Data
public class UserMenuDTO {
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
     * 0未禁用，1已禁用
     */
    private Boolean disabledFlag;

    /**
     * 子菜单列表
     */
    private List<UserMenuDTO> children;
}