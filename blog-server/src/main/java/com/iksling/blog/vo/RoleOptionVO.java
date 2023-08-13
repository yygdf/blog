package com.iksling.blog.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoleOptionVO {
    /**
     * 角色id
     */
    @NotNull(message = "角色id不能为空")
    @ApiModelProperty(name = "id", value = "角色id", dataType = "Integer")
    private Integer id;

    /**
     * 菜单id集合
     */
    @ApiModelProperty(name = "menuIdList", value = "菜单id集合", dataType = "List<Integer>")
    private List<Integer> menuIdList;

    /**
     * 资源id集合
     */
    @ApiModelProperty(name = "resourceIdList", value = "资源id集合", dataType = "List<Integer>")
    private List<Integer> resourceIdList;
}
