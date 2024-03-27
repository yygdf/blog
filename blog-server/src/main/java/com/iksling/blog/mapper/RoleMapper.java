package com.iksling.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.iksling.blog.dto.RolesBackDTO;
import com.iksling.blog.entity.Role;

import java.util.List;

/**
 * @Entity com.iksling.blog.entity.Role
 */
public interface RoleMapper extends BaseMapper<Role> {

    List<Role> selectLoginRoleByUserId(Integer userId);

    List<RolesBackDTO> selectRolesBackDTO(String keywords);
}




