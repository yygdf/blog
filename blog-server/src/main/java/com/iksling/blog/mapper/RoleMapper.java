package com.iksling.blog.mapper;

import com.iksling.blog.entity.Role;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * @Entity com.iksling.blog.entity.Role
 */
public interface RoleMapper extends BaseMapper<Role> {

    List<String> listRolesByUserId(Integer userId);
}




