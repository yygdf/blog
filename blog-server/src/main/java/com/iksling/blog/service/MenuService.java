package com.iksling.blog.service;

import com.iksling.blog.dto.UserMenuDTO;
import com.iksling.blog.entity.Menu;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 *
 */
public interface MenuService extends IService<Menu> {

    List<UserMenuDTO> listUserMenus();
}
