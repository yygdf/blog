package com.iksling.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.iksling.blog.dto.RolesBackDTO;
import com.iksling.blog.entity.Role;

import java.util.List;
import java.util.Map;

/**
 * @Entity com.iksling.blog.entity.Role
 */
public interface RoleMapper extends BaseMapper<Role> {

    @SuppressWarnings("MybatisXMapperMethodInspection")
    List<Map<String, Object>> selectLoginRoleByUserId(Integer userId);

    List<RolesBackDTO> selectRolesBackDTO(String keywords);
}




