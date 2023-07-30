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
public class CommentsBackDTO {
    /**
     * 评论id
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
     * 评论人昵称
     */
    private String nickname;

    /**
     * 回复人昵称
     */
    private String replyNickname;

    /**
     * 点赞量
     */
    private Integer likeCount;
}
