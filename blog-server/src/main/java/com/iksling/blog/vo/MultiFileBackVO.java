package com.iksling.blog.vo;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.util.List;

@Data
public class MultiFileBackVO implements Serializable {
    /**
     * 文件id
     */
    private Integer id;

    /**
     * 父目录id
     */
    private Integer multiDirId;

    /**
     * 子目录
     */
    private String subDir;

    /**
     * 文件地址
     */
    private String fileUrl;

    /**
     * 文件描述
     */
    private String fileDesc;

    /**
     * 文件名称
     */
    private String fileName;

    /**
     * 0未隐藏，1已隐藏
     */
    private Boolean isHidden;

    /**
     * 文件
     */
    private List<MultipartFile> file;
}