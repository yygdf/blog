package com.iksling.blog.dto;

import lombok.Data;

@Data
public class UsernameDTO {
    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 用户名
     */
    private String username;
}
