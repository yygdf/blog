package com.iksling.blog.dto;

import lombok.Data;

import java.util.Date;

@Data
public class UserConfigsBackDTO {
    /**
     * id
     */
    private Integer id;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 配置描述
     */
    private String configDesc;

    /**
     * 配置值
     */
    private String configValue;

    /**
     * 配置名
     */
    private String configName;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;
}