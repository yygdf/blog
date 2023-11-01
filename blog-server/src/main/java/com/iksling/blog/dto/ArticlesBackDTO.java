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
public class ArticlesBackDTO {
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
     * 文章标题
     */
    private String articleTitle;

    /**
     * 0未置顶, 1已置顶
     */
    private Boolean topFlag;

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
     * 发表时间
     */
    private Date publishTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 点赞量
     */
    private Integer likeCount;

    /**
     * 浏览量
     */
    private Integer viewCount;

    /**
     * 分类名
     */
    private String categoryName;

    /**
     * 标签名list字符串
     */
    private String tagNameList;
}
