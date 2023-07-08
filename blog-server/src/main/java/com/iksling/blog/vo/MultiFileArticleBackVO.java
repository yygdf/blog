package com.iksling.blog.vo;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

@Data
public class MultiFileArticleBackVO implements Serializable {
    /**
     * 文件
     */
    private MultipartFile file;

    /**
     * 子目录
     */
    private String fileSubDir;
}