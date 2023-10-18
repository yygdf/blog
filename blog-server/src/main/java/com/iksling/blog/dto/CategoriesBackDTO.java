package com.iksling.blog.dto;

import lombok.Data;

import java.util.Date;

@Data
public class CategoriesBackDTO {
    /**
     * 分类id
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
     * 分类名
     */
    private String categoryName;

    /**
     * 0未公开，1已公开
     */
    private Boolean publicFlag;

    /**
     * 0未隐藏，1已隐藏
     */
    private Boolean hiddenFlag;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 文章数量
     */
    private Integer articleCount;
}