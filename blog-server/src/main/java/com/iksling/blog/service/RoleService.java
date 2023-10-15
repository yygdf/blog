package com.iksling.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.iksling.blog.dto.LabelBackDTO;
import com.iksling.blog.dto.RoleOptionBackDTO;
import com.iksling.blog.dto.RolesBackDTO;
import com.iksling.blog.entity.Role;
import com.iksling.blog.vo.StatusBackVO;
import com.iksling.blog.vo.RoleBackVO;
import com.iksling.blog.vo.RoleOptionBackVO;

import java.util.List;

/**
 *
 */
public interface RoleService extends IService<Role> {

    List<RolesBackDTO> getRolesBackDTO(String keywords);

    RoleOptionBackDTO getRoleOptionDTO();

    void updateRoleStatusVO(StatusBackVO statusBackVO);

    void deleteRoleById(String id);

    void saveOrUpdateRoleBackVO(RoleBackVO roleBackVO);

    void updateRoleOptionVO(RoleOptionBackVO roleOptionBackVO);

    List<LabelBackDTO> getBackRoleNames();
}
