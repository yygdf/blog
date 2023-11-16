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
    @NotNull(message = "'id':'NotNull'")
    @ApiModelProperty(name = "id", value = "角色id", dataType = "Integer")
    private Integer id;

    /**
     * 菜单idList
     */
    @Size(min = 1, message = "'menuIdList':{'min':1}")
    @ApiModelProperty(name = "menuIdList", value = "菜单idList", dataType = "List<Integer>")
    private List<Integer> menuIdList;

    /**
     * 资源idList
     */
    @Size(min = 1, message = "'resourceIdList':{'min':1}")
    @ApiModelProperty(name = "resourceIdList", value = "资源idList", dataType = "List<Integer>")
    private List<Integer> resourceIdList;
}
