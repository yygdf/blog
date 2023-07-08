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
    private String resourceRequestMethod;

    /**
     * 0未禁用，1已禁用
     */
    private Boolean disabledFlag;

    /**
     * 0未匿名，1已匿名
     */
    private Boolean anonymousFlag;

    /**
     * 允许访问的角色
     */
    private List<String> roleList;
}
