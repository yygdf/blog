package com.iksling.blog.pojo;

import lombok.Data;

@Data
public class LoginRole {
    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 角色权重
     */
    private Integer roleWeight;
}