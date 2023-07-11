package com.iksling.blog.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@ApiModel(description = "文章垃圾VO")
public class ArticlesGarbageVO {
    /**
     * id列表
     */
    @NotEmpty(message = "id列表不能为空")
    @ApiModelProperty(name = "idList", value = "id列表", required = true, dataType = "List<Integer>")
    private List<Integer> idList;

    /**
     * 是否为垃圾
     */
    @NotNull(message = "垃圾标志不能为null")
    @ApiModelProperty(name = "garbageFlag", value = "是否为垃圾", required = true, dataType = "Boolean")
    private Boolean garbageFlag;
}
