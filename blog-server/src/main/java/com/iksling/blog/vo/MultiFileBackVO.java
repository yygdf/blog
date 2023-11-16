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
    @ApiModelProperty(name = "parentId", value = "父资源id", dataType = "Integer")
    private Integer parentId;

    /**
     * 文件描述
     */
    @Size(max = 50, message = "'fileDesc':{'maxlength':50}")
    @ApiModelProperty(name = "fileDesc", value = "文件描述", dataType = "String")
    private String fileDesc;

    /**
     * 文件封面
     */
    @Size(max = 255, message = "'fileCover':{'maxlength':255}")
    @ApiModelProperty(name = "fileCover", value = "文件封面", dataType = "String")
    private String fileCover;

    /**
     * 文件原始名称
     */
    @Size(min = 1, max = 50, message = "'fileNameOrigin':{'minlength':1,'maxlength':50}")
    @ApiModelProperty(name = "fileNameOrigin", value = "文件原始名称", dataType = "String")
    private String fileNameOrigin;

    /**
     * 0未公开, 1已公开
     */
    @ApiModelProperty(name = "publicFlag", value = "0未公开, 1已公开", dataType = "Boolean")
    private Boolean publicFlag;

    /**
     * 0未隐藏, 1已隐藏
     */
    @ApiModelProperty(name = "hiddenFlag", value = "0未隐藏, 1已隐藏", dataType = "Boolean")
    private Boolean hiddenFlag;
}