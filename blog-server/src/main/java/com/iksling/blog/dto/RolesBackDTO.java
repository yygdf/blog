package com.iksling.blog.dto;

import lombok.Data;

import java.util.Date;

@Data
public class RolesBackDTO {
    /**
     * 角色id
     */
    private Integer id;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 角色描述
     */
    private String roleDesc;

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 角色权重
     */
    private Integer roleWeight;

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
     * 用户数量
     */
    private Integer userCount;

    /**
     * 菜单idList
     */
    private String menuIdList;

    /**
     * 资源idList
     */
    private String resourceIdList;
}