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
 * @TableName tb_comment
 */
@TableName(value ="tb_comment")
@Data
public class Comment implements Serializable {
    /**
     * 评论id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 文章id
     */
    private Integer articleId;

    /**
     * 评论内容
     */
    private String commentContent;

    /**
     * 回复用户id
     */
    private Integer replyId;

    /**
     * 父评论id
     */
    private Integer parentId;

    /**
     * 0未删除，1已删除
     */
    private Boolean isDeleted;

    /**
     * 创建时间
     */
    private Date createTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}