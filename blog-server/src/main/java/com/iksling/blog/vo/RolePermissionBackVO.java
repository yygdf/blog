package com.iksling.blog.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RolePermissionBackVO {
    /**
     * id
     */
    @NotNull(message = "{V0031}")
    @ApiModelProperty(name = "id", value = "角色id", dataType = "Integer")
    private Integer id;

    /**
     * 菜单id列表
     */
    @Size(min = 1, message = "{V0057}")
    @ApiModelProperty(name = "menuIdList", value = "菜单id列表", dataType = "List<Integer>")
    private List<Integer> menuIdList;

    /**
     * 资源id列表
     */
    @Size(min = 1, message = "{V0058}")
    @ApiModelProperty(name = "resourceIdList", value = "资源id列表", dataType = "List<Integer>")
    private List<Integer> resourceIdList;
}
