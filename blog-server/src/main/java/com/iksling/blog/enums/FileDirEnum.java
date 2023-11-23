package com.iksling.blog.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FileDirEnum {
    /**
     * 图片目录
     */
    IMAGE("image", "-1", -1L, 10, "MB"),

    /**
     * 音频目录
     */
    AUDIO("audio", "-2", -2L, 10, "MB"),

    /**
     * 其他目录
     */
    OTHER("other", "-9", -9L, 100, "MB"),

    /**
     * 用户头像目录
     */
    IMAGE_AVATAR("avatar", "-1/-11", -11L, 5, "MB"),

    /**
     * 文章图片目录
     */
    IMAGE_ARTICLE("article", "-1/-12", -12L, 5, "MB"),

    /**
     * 相册目录
     */
    IMAGE_ALBUM("album", "-1/-13", -13L, 500, "MB"),

    /**
     * 聊天室音频目录
     */
    AUDIO_CHAT("chat", "-2/-21", -21L, 5, "MB"),

    /**
     * 音乐播放器目录
     */
    AUDIO_MUSIC("music", "-2/-22", -22L, 10, "MB");

    /**
     * 名称
     */
    private final String name;

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
}
