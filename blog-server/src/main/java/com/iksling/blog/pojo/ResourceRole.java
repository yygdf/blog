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
    private Integer isDisabled;

    /**
     * 0不能匿名，1可以匿名
     */
    private Integer isAnonymous;

    /**
     * 允许访问的角色
     */
    private List<String> roleList;
}
