package com.iksling.blog.pojo;

import lombok.Data;

@Data
public class ArticleImgFile {
    private Integer id;

    private Integer userId;

    private String dirPath;

    private String fileExtension;
}
