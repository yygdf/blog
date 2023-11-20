package com.iksling.blog.dto;

import lombok.Data;

import java.util.Date;

@Data
public class MultiFilesBackDTO {
    /**
     * id
     */
    private Integer id;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 父id
     */
    private Integer parentId;

    /**
     * 文件描述
     */
    private String fileDesc;

    /**
     * 文件大小
     */
    private Long fileSize;

    /**
     * 文件封面
     */
    private String fileCover;

    /**
     * 文件完整路径
     */
    private String fileFullPath;

    /**
     * 文件拓展名
     */
    private String fileExtension;

    /**
     * 文件原始名称
     */
    private String fileNameOrigin;

    /**
     * 0未公开, 1已公开
     */
    private Boolean publicFlag;

    /**
     * 0未隐藏, 1已隐藏
     */
    private Boolean hiddenFlag;

    /**
     * 0不可删除, 1可删除
     */
    private Boolean deletableFlag;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 是否有子文件
     */
    private Boolean hasChildren;
}