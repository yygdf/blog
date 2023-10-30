package com.iksling.blog.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

@Data
@ApiModel(description = "用户头像后台VO")
public class UserAvatarBackVO {
    /**
     * 文件
     */
    @NotNull(message = "'file':'NotNull'")
    @ApiModelProperty(name = "file", value = "文件", required = true, dataType = "MultiPartFile")
    private MultipartFile file;

    /**
     * 用户id
     */
    @ApiModelProperty(name = "userId", value = "用户id", dataType = "Integer")
    private Integer userId;
}