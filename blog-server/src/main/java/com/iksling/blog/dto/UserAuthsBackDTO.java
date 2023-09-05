package com.iksling.blog.dto;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class UserAuthsBackDTO {
    /**
     * 账号id
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
     * 登录时间
     */
    private Date loginTime;

    /**
     * 登录方式
     */
    private Integer loginMethod;

    /**
     * 0未锁定，1已锁定
     */
    private Boolean lockedFlag;

    /**
     * 0未禁用，1已禁用
     */
    private Boolean disabledFlag;

    /**
     * ip来源
     */
    private String ipSource;

    /**
     * ip地址
     */
    private String ipAddress;

    /**
     * 用户角色
     */
    private List<LabelDTO> roleDTOList;
}