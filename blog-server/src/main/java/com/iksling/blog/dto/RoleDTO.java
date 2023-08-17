package com.iksling.blog.dto;

import lombok.Data;

@Data
public class RoleDTO {
    /**
     * 角色id
     */
    private Integer id;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 角色名称
     */
    private String roleName;
}
