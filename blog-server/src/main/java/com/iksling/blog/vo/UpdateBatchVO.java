package com.iksling.blog.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@ApiModel(description = "批量更新VO")
public class UpdateBatchVO {
    /**
     * id列表
     */
    @NotEmpty(message = "id列表不能为空")
    @ApiModelProperty(name = "idList", value = "id列表", required = true, dataType = "List<Integer>")
    private List<Integer> idList;

    /**
     * 0未回收，1已回收，默认0
     */
    @ApiModelProperty(name = "recycleFlag", value = "0未回收，1已回收，默认0", dataType = "Boolean")
    private Boolean recycleFlag;

    /**
     * 0未删除，1已删除，默认0
     */
    @NotNull(message = "删除标志不能为null")
    @ApiModelProperty(name = "deletedFlag", value = "0未删除，1已删除，默认0", required = true, dataType = "Boolean")
    private Boolean deletedFlag;
}
