package com.iksling.blog.dto;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class ResourcesBackDTO {
    /**
     * id
     */
    private Integer id;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 父id
     */
    private Integer parentId;

    /**
     * 资源路径
     */
    private String resourceUri;

    /**
     * 资源名称
     */
    private String resourceName;

    /**
     * 资源名称(en)
     */
    private String resourceNameEn;

    /**
     * 请求方式
     */
    private String resourceRequestMethod;

    /**
     * 0未禁用, 1已禁用
     */
    private Boolean disabledFlag;

    /**
     * 0不可删除, 1可删除
     */
    private Boolean deletableFlag;

    /**
     * 0未匿名, 1已匿名
     */
    private Boolean anonymousFlag;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 子资源
     */
    private List<ResourcesBackDTO> children;
}