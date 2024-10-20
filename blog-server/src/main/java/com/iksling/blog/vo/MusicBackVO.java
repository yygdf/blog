package com.iksling.blog.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Size;

@Data
@ApiModel(description = "音乐后台VO")
public class MusicBackVO {
    /**
     * id
     */
    @ApiModelProperty(name = "id", value = "音乐id", dataType = "Integer")
    private Integer id;

    /**
     * 音乐链接
     */
    @Size(min = 1, max = 255, message = "{V0034}")
    @ApiModelProperty(name = "musicUrl", value = "音乐链接", dataType = "String")
    private String musicUrl;

    /**
     * 音乐名称
     */
    @Size(min = 1, max = 50, message = "{V0035}")
    @ApiModelProperty(name = "musicName", value = "音乐名称", dataType = "String")
    private String musicName;

    /**
     * 音乐封面
     */
    @Size(max = 255, message = "{V0036}")
    @ApiModelProperty(name = "musicCover", value = "音乐封面", dataType = "String")
    private String musicCover;

    /**
     * 音乐歌词
     */
    @ApiModelProperty(name = "musicWords", value = "音乐歌词", dataType = "String")
    private String musicWords;

    /**
     * 作者
     */
    @Size(max = 50, message = "{V0037}")
    @ApiModelProperty(name = "author", value = "作者", dataType = "String")
    private String author;

    /**
     * 专辑
     */
    @Size(max = 50, message = "{V0038}")
    @ApiModelProperty(name = "album", value = "专辑", dataType = "String")
    private String album;
}