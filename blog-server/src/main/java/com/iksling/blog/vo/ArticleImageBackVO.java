package com.iksling.blog.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

@Data
@ApiModel(description = "文章图片后台VO")
public class ArticleImageBackVO {
    /**
     * 文件
     */
    @NotNull(message = "{V0004}")
    @ApiModelProperty(name = "file", value = "文件", required = true, dataType = "MultiPartFile")
    private MultipartFile file;

    /**
     * 用户id
     */
    @ApiModelProperty(name = "userId", value = "用户id", dataType = "Integer")
    private Integer userId;

    /**
     * 文章id
     */
    @NotNull(message = "{V0005}")
    @ApiModelProperty(name = "articleId", value = "文章id", required = true, dataType = "Integer")
    private Integer articleId;
}