package com.iksling.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.iksling.blog.dto.MenusBackDTO;
import com.iksling.blog.dto.RoleOptionsDTO;
import com.iksling.blog.dto.UserMenusDTO;
import com.iksling.blog.entity.Menu;
import com.iksling.blog.vo.CommonStatusVO;
import com.iksling.blog.vo.MenuBackVO;

import java.util.List;

/**
 *
 */
public interface MenuService extends IService<Menu> {

    List<UserMenusDTO> getUserMenusDTO();

    List<MenusBackDTO> getMenusBackDTO(String keywords);

    void updateMenuStatusVO(CommonStatusVO commonStatusVO);

    void deleteMenuById(String id);

    void saveOrUpdateMenuBackVO(MenuBackVO menuBackVO);

    List<RoleOptionsDTO> getMenusDTO();
}
