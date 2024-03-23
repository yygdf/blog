package com.iksling.blog.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginUserDTO {
    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 用户介绍
     */
    private String intro;

    /**
     * 用户邮箱
     */
    private String email;

    /**
     * 用户头像
     */
    private String avatar;

    /**
     * 用户性别
     */
    private Integer gender;

    /**
     * 用户网站
     */
    private String website;

    /**
     * 用户昵称
     */
    private String nickname;

    /**
     * 点赞文章集合
     */
    private Set<Integer> articleLikeSet;

    /**
     * 点赞评论集合
     */
    private Set<Integer> commentLikeSet;
}
