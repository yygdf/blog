package com.iksling.blog.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@ApiModel(description = "文件后台VO")
public class MultiFilesBackVO {
    /**
     * id
     */
    @NotNull(message = "{V0031}")
    @ApiModelProperty(name = "id", value = "文件id", required = true, dataType = "Integer")
    private Integer id;

    /**
     * 用户id
     */
    @ApiModelProperty(name = "userId", value = "用户id", dataType = "Integer")
    private Integer userId;

    /**
     * 文件列表
     */
    @NotNull(message = "{V0032}")
    @Size(min = 1, max = 10, message = "{V0033}")
    @ApiModelProperty(name = "fileList", value = "文件列表", required = true, dataType = "List<MultiPartFile>")
    private List<MultipartFile> fileList;
}