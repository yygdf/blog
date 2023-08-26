package com.iksling.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.iksling.blog.dto.LabelDTO;
import com.iksling.blog.dto.RoleOptionDTO;
import com.iksling.blog.dto.RolesBackDTO;
import com.iksling.blog.entity.Role;
import com.iksling.blog.vo.CommonStatusVO;
import com.iksling.blog.vo.RoleBackVO;
import com.iksling.blog.vo.RoleOptionVO;

import java.util.List;

/**
 *
 */
public interface RoleService extends IService<Role> {

    List<RolesBackDTO> getRolesBackDTO(String keywords);

    RoleOptionDTO getRoleOptionDTO();

    void updateRoleStatusVO(CommonStatusVO commonStatusVO);

    void deleteRoleById(String id);

    void saveOrUpdateRoleBackVO(RoleBackVO roleBackVO);

    void updateRoleOptionVO(RoleOptionVO roleOptionVO);

    List<LabelDTO> getBackRoleNames();
}
