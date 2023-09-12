package com.iksling.blog.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

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
     * 用户id
     */
    private Integer userId;

    /**
     * 分类id，默认-1
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
     * 0未置顶，1已置顶，默认0
     */
    private Boolean topFlag;

    /**
     * 0不是草稿，1是草稿，默认1
     */
    private Boolean draftFlag;

    /**
     * 0未公开，1已公开，默认1
     */
    private Boolean publicFlag;

    /**
     * 0未隐藏，1已隐藏，默认0
     */
    private Boolean hiddenFlag;

    /**
     * 0未回收，1已回收，默认0
     */
    private Boolean recycleFlag;

    /**
     * 0未删除，1已删除，默认0
     */
    private Boolean deletedFlag;

    /**
     * 0不可评论，1可评论，默认1
     */
    private Boolean commentableFlag;

    /**
     * ip来源，默认空串
     */
    private String ipSource;

    /**
     * ip地址，默认空串
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
     * 更新人，默认null
     */
    private Integer updateUser;

    /**
     * 更新时间，默认null
     */
    private Date updateTime;

    /**
     * 发表人，默认null
     */
    private Integer publishUser;

    /**
     * 发表时间，默认null
     */
    private Date publishTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}