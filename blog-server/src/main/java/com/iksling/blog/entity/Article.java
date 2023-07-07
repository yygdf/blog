package com.iksling.blog.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName tb_article
 */
@TableName(value ="tb_article")
@Data
public class Article implements Serializable {
    /**
     * 文章id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 作者id
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
    private Integer isTop;

    /**
     * 0不是草稿，1是草稿
     */
    private Boolean isDraft;

    /**
     * 0未公开，1已公开
     */
    private Boolean isPublic;

    /**
     * 0未隐藏，1已隐藏
     */
    private Boolean isHidden;

    /**
     * 0未删除，1已删除
     */
    private Boolean isDeleted;

    /**
     * 0不可评论，1可评论
     */
    private Boolean isCommentable;

    /**
     * ip来源
     */
    private String ipSource;

    /**
     * ip地址
     */
    private String ipAddress;

    /**
     * 创建人
     */
    private Integer createUser;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新人
     */
    private Integer updateUser;

    /**
     * 更新时间
     */
    private Date updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}