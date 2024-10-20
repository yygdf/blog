package com.iksling.blog.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Size;

@Data
@ApiModel(description = "文件后台VO")
public class MultiFileBackVO {
    /**
     * id
     */
    @ApiModelProperty(name = "id", value = "文件id", dataType = "Integer")
    private Integer id;

    /**
     * 父id
     */
    @ApiModelProperty(name = "parentId", value = "父id", dataType = "Integer")
    private Integer parentId;

    /**
     * 文件描述
     */
    @Size(max = 50, message = "{V0028}")
    @ApiModelProperty(name = "fileDesc", value = "文件描述", dataType = "String")
    private String fileDesc;

    /**
     * 文件封面
     */
    @Size(max = 255, message = "{V0029}")
    @ApiModelProperty(name = "fileCover", value = "文件封面", dataType = "String")
    private String fileCover;

    /**
     * 文件原始名称
     */
    @Size(min = 1, max = 50, message = "{V0030}")
    @ApiModelProperty(name = "fileNameOrigin", value = "文件原始名称", dataType = "String")
    private String fileNameOrigin;
}