package com.iksling.blog.dto;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class CommentsDTO {
    /**
     * id
     */
    private Integer id;

    /**
     * 用户id
     */
    private Integer userId;

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
     * 点赞量
     */
    private Integer likeCount;

    /**
     * 回复量
     */
    private Integer replyCount;

    /**
     * 回复评论列表
     */
    private List<CommentsReplyDTO> commentsReplyDTOList;
}
