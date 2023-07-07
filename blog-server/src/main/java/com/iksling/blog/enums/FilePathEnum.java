package com.iksling.blog.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FilePathEnum {
    /**
     * 聊天室音频目录
     */
    VOICE(1, "voice/chat/", "聊天室音频目录", 1, "MB"),

    /**
     * 用户头像目录
     */
    AVATAR(2, "img/avatar/", "用户头像目录", 1, "MB"),

    /**
     * 文章图片目录
     */
    ARTICLE(3, "img/article/", "文章图片目录", 5, "MB");

    /**
     * id
     */
    private final Integer id;

    /**
     * 路径
     */
    private final String path;

    /**
     * 描述
     */
    private final String desc;

    /**
     * 大小
     */
    private final Integer size;

    /**
     * 单位
     */
    private final String unit;
}
