package com.iksling.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.iksling.blog.dto.LabelBackDTO;
import com.iksling.blog.dto.RolePermissionBackDTO;
import com.iksling.blog.dto.RolesBackDTO;
import com.iksling.blog.entity.Role;
import com.iksling.blog.vo.StatusBackVO;
import com.iksling.blog.vo.RoleBackVO;
import com.iksling.blog.vo.RolePermissionBackVO;

import java.util.List;

/**
 *
 */
public interface RoleService extends IService<Role> {

    void saveOrUpdateRoleBackVO(RoleBackVO roleBackVO);

    void deleteBackRolesByIdList(List<Integer> idList);

    void updateRoleStatusBackVO(StatusBackVO statusBackVO);

    void updateRolePermissionBackVO(RolePermissionBackVO rolePermissionBackVO);

    List<RolesBackDTO> getRolesBackDTO(String keywords);

    List<LabelBackDTO> getBackRoleNames();

    RolePermissionBackDTO getRolePermissionBackDTO();
}
