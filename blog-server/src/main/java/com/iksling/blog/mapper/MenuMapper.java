package com.iksling.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.iksling.blog.entity.Menu;

import java.util.List;

/**
 * @Entity com.iksling.blog.entity.Menu
 */
public interface MenuMapper extends BaseMapper<Menu> {

    List<Menu> listMenusByUserId(Integer userId, Integer roleWeight);

    List<Menu> listMenusByKeywords(String keywords);
}




