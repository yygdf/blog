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
     * 文章标题
     */
    private String articleTitle;

    /**
     * 文章内容
     */
    private String articleContent;

    /**
     * 文章封面
     */
    private String articleCover;

    /**
     * 分类id
     */
    private Integer categoryId;

    /**
     * 0未置顶，1已置顶
     */
    private Integer isTop;

    /**
     * 0是草稿，1不是草稿
     */
    private Integer isDraft;

    /**
     * 0未删除，1已删除
     */
    private Integer isDeleted;

    /**
     * 创建时间
     */
    private Date creatTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}