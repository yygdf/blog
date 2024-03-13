package com.iksling.blog.dto;

import lombok.Data;

import java.util.Date;

@Data
public class ArticlesPreviewDTO {
    /**
     * id
     */
    private Integer id;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 分类id
     */
    private Integer categoryId;

    /**
     * 文章标题
     */
    private String articleTitle;

    /**
     * 文章封面
     */
    private String articleCover;

    /**
     * 0未公开, 1已公开
     */
    private Boolean publicFlag;

    /**
     * 0未隐藏, 1已隐藏
     */
    private Boolean hiddenFlag;

    /**
     * 发表时间
     */
    private Date publishTime;

    /**
     * 分类名
     */
    private String categoryName;

    /**
     * 标签list字符串
     */
    private String tagList;
}
