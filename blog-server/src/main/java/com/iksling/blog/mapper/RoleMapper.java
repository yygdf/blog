package com.iksling.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.iksling.blog.dto.RolesBackDTO;
import com.iksling.blog.entity.Role;
import com.iksling.blog.pojo.LoginRole;

import java.util.List;

/**
 * @Entity com.iksling.blog.entity.Role
 */
public interface RoleMapper extends BaseMapper<Role> {

    List<LoginRole> listLoginRoleByUserId(Integer userId);

    List<RolesBackDTO> listRolesBackDTO(String keywords);
}




