package com.iksling.blog.pojo;

import lombok.Data;

import java.util.List;

@Data
public class ResourceRole {
    /**
     * 资源id
     */
    private Integer id;

    /**
     * 资源路径
     */
    private String resourceUri;

    /**
     * 请求方式
     */
    private String requestMethod;

    /**
     * 0未禁用，1已禁用
     */
    private Boolean isDisabled;

    /**
     * 0未匿名，1已匿名
     */
    private Boolean isAnonymous;

    /**
     * 允许访问的角色
     */
    private List<String> roleList;
}
