package com.iksling.blog.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
@ApiModel(description = "文章垃圾VO")
public class ArticlesGarbageVO {
    /**
     * id列表
     */
    @NotBlank(message = "id列表不能为空")
    @ApiModelProperty(name = "idList", value = "id列表", required = true, dataType = "List<Integer>")
    private List<Integer> idList;

    /**
     * 是否为垃圾
     */
    @NotBlank(message = "垃圾标志不能为空")
    @ApiModelProperty(name = "garbageFlag", value = "是否为垃圾", required = true, dataType = "Integer")
    private Boolean garbageFlag;
}
