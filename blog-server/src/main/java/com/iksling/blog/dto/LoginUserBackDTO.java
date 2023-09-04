package com.iksling.blog.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginUserBackDTO {
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
     * 权重
     */
    private Integer weight;

    /**
     * 用户网站
     */
    private String website;

    /**
     * 用户昵称
     */
    private String nickname;

    /**
     * 系统核心用户账号idList
     */
    public List<Integer> rootUserAuthId;
}
