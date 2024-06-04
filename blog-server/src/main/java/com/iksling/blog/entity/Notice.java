package com.iksling.blog.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @TableName tb_notice
 */
@TableName(value ="tb_notice")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notice implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 文章id, 默认-1
     */
    private Integer articleId;

    /**
     * 评论id, 默认-1
     */
    private Integer commentId;

    /**
     * 通知类型, 1回复我的, 2@ 我的, 3收到的赞, 4系统通知, 5我的消息
     */
    private Integer noticeType;

    /**
     * 通知子类型, 默认-1
     */
    private Integer noticeTypeSub;

    /**
     * 通知标题, 默认空串
     */
    private String noticeTitle;

    /**
     * 通知内容, 默认空串
     */
    private String noticeContent;

    /**
     * 0未读, 1已读, 默认0
     */
    private Boolean readFlag;

    /**
     * 0未删除, 1已删除, 默认0
     */
    private Boolean deletedFlag;

    /**
     * 创建人
     */
    private Integer createUser;

    /**
     * 创建时间
     */
    private Date createTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}