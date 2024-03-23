package com.iksling.blog.dto;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class ArticleDTO {
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
     * 文章内容
     */
    private String articleContent;

    /**
     * 0未公开, 1已公开
     */
    private Boolean publicFlag;

    /**
     * 0未隐藏, 1已隐藏
     */
    private Boolean hiddenFlag;

    /**
     * 0不可评论, 1可评论
     */
    private Boolean commentableFlag;

    /**
     * 更新时间
     */
    private Date updateTime;

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

    /**
     * 点赞量
     */
    private Integer likeCount;

    /**
     * 浏览量
     */
    private Integer viewCount;

    /**
     * 0未允许, 1已允许
     */
    private Boolean permitFlag;

    /**
     * 上一篇文章
     */
    private ArticlesPaginationDTO lastArticle;

    /**
     * 下一篇文章
     */
    private ArticlesPaginationDTO nextArticle;

    /**
     * 推荐文章
     */
    private List<ArticlesRecommendDTO> articlesRecommendDTOList;
}
