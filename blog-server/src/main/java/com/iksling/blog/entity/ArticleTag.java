package com.iksling.blog.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

import lombok.Builder;
import lombok.Data;

/**
 * 
 * @TableName tb_article_tag
 */
@TableName(value ="tb_article_tag")
@Data
@Builder
public class ArticleTag implements Serializable {
    /**
     * 主键id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 标签id
     */
    private Integer tagId;

    /**
     * 文章id
     */
    private Integer articleId;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}