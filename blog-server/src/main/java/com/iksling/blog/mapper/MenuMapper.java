package com.iksling.blog.mapper;

import com.iksling.blog.entity.Menu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * @Entity com.iksling.blog.entity.Menu
 */
public interface MenuMapper extends BaseMapper<Menu> {

    List<Menu> listMenusByUserId(Integer userId);
}




