package com.iksling.blog.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FileEnum {
    /**
     * 图片目录枚举对象
     */
    IMAGE( "-1", -1L, 5, "MB", "图片目录", "IMAGE"),

    /**
     * 音频目录枚举对象
     */
    AUDIO("-2", -2L, 1, "MB", "音频目录", "AUDIO"),

    /**
     * 用户头像目录枚举对象
     */
    IMAGE_AVATAR("-1/-3", -3L, 1, "MB", "用户头像目录", "IMAGE"),

    /**
     * 聊天室音频目录枚举对象
     */
    AUDIO_CHAT("-2/-4", -4L, 1, "MB", "聊天室音频目录", "AUDIO"),

    /**
     * 文章图片目录枚举对象
     */
    IMAGE_ARTICLE("-1/-5", -5L, 5, "MB", "文章图片目录", "IMAGE");

    /**
     * 路径
     */
    private final String path;

    /**
     * 路当前径
     */
    private final Long currentPath;

    /**
     * 大小
     */
    private final Integer size;

    /**
     * 单位
     */
    private final String unit;

    /**
     * 描述
     */
    private final String desc;

    /**
     * 类型
     */
    private final String type;
}
