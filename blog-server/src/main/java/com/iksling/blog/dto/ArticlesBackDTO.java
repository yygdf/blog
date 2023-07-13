package com.iksling.blog.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticlesBackDTO {
    /**
     * 文章id
     */
    private Integer id;

    /**
     * 作者id
     */
    private Integer userId;

    /**
     * 文章标题
     */
    private String articleTitle;

    /**
     * 0未置顶，1已置顶
     */
    private Boolean topFlag;

    /**
     * 0是草稿，1不是草稿
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
     * 0不是垃圾，1是垃圾
     */
    private Boolean garbageFlag;

    /**
     * 0不可评论，1可评论
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
     * 文章标签
     */
    private List<TagDTO> tagDTOList;
}
