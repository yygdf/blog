package com.iksling.blog.dto;

import lombok.Data;

@Data
public class ArticleBackDTO {
    /**
     * 文章id
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
     * 文章内容
     */
    private String articleContent;

    /**
     * 0未置顶，1已置顶
     */
    private Boolean topFlag;

    /**
     * 0不是草稿，1是草稿
     */
    private Boolean draftFlag;

    /**
     * 0未公开，1已公开
     */
    private Boolean publicFlag;

    /**
     * 0未隐藏，1已隐藏
     */
    private Boolean hiddenFlag;

    /**
     * 0不可评论，1可评论
     */
    private Boolean commentableFlag;

    /**
     * 标签idList字符串
     */
    private String tagIdList;
}
