package com.iksling.blog.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FilePathEnum {
    /**
     * 图片目录枚举对象
     */
    IMG(1, "img", "图片目录"),

    /**
     * 音频目录枚举对象
     */
    AUDIO(2, "audio", "音频目录"),

    /**
     * 用户头像目录枚举对象
     */
    IMG_AVATAR(3, "img/avatar", "用户头像目录"),

    /**
     * 文章图片目录枚举对象
     */
    IMG_ARTICLE(4, "img/article", "文章图片目录"),

    /**
     * 聊天室音频目录枚举对象
     */
    AUDIO_CHAT(5, "audio/chat", "聊天室音频目录");

    /**
     * 标识
     */
    private final Integer mark;

    /**
     * 路径
     */
    private final String path;

    /**
     * 描述
     */
    private final String desc;
}
