package com.iksling.blog.mapper;

import com.iksling.blog.entity.Resource;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.iksling.blog.pojo.ResourceRole;

import java.util.List;

/**
 * @Entity com.iksling.blog.entity.Resource
 */
public interface ResourceMapper extends BaseMapper<Resource> {

    List<ResourceRole> listResourceRoles();
}




