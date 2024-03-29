package com.iksling.blog.dto;

import lombok.Data;

import java.util.Date;

@Data
public class CommentsReplyDTO {
    /**
     * id
     */
    private Integer id;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 回复用户id
     */
    private Integer replyId;

    /**
     * 父id
     */
    private Integer parentId;

    /**
     * 评论内容
     */
    private String commentContent;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 评论人头像
     */
    private String avatar;

    /**
     * 评论人网站
     */
    private String website;

    /**
     * 评论人昵称
     */
    private String nickname;

    /**
     * 回复用户网站
     */
    private String replyWebsite;

    /**
     * 回复用户昵称
     */
    private String replyNickname;

    /**
     * 点赞量
     */
    private Integer likeCount;
}
