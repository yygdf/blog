package com.iksling.blog.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoleOptionDTO {
    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 角色菜单集合
     */
    private List<RoleOptionsDTO> menusDTOList;

    /**
     * 角色资源集合
     */
    private List<RoleOptionsDTO> resourcesDTOList;
}
