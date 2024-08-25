package com.iksling.blog.dto;

import lombok.Data;

@Data
public class MusicsDTO {
    /**
     * id
     */
    private Integer id;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 音乐链接
     */
    private String musicUrl;

    /**
     * 音乐名称
     */
    private String musicName;

    /**
     * 音乐封面
     */
    private String musicCover;

    /**
     * 音乐歌词
     */
    private String musicWords;

    /**
     * 作者
     */
    private String author;

    /**
     * 专辑
     */
    private String album;
}