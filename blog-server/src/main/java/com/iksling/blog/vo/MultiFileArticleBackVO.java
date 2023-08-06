package com.iksling.blog.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@ApiModel(description = "文章文件后台VO")
public class MultiFileArticleBackVO  {
    /**
     * 文件
     */
    @NotNull(message = "文件不能为null")
    @ApiModelProperty(name = "file", value = "文件", required = true, dataType = "MultiPartFile")
    private MultipartFile file;

    /**
     * 用户id
     */
    @ApiModelProperty(name = "userId", value = "用户id", dataType = "Integer")
    private Integer userId;

    /**
     * 文件子目录
     */
    @NotBlank(message = "文件子目录不能为空")
    @ApiModelProperty(name = "fileSubDir", value = "文件子目录", required = true, dataType = "String")
    private String fileSubDir;
}