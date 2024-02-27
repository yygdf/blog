package com.iksling.blog.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticlesDTO {
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
     * 文章内容
     */
    private String articleContent;

    /**
     * 0未置顶, 1已置顶
     */
    private Boolean topFlag;

    /**
     * 0未公开, 1已公开, 默认1
     */
    private Boolean publicFlag;

    /**
     * 0未隐藏, 1已隐藏, 默认0
     */
    private Boolean hiddenFlag;

    /**
     * 0不可评论, 1可评论, 默认1
     */
    private Boolean commentableFlag;

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
