package com.iksling.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.iksling.blog.dto.MenusBackDTO;
import com.iksling.blog.dto.LabelsBackDTO;
import com.iksling.blog.dto.UserMenusBackDTO;
import com.iksling.blog.entity.Menu;
import com.iksling.blog.vo.StatusBackVO;
import com.iksling.blog.vo.MenuBackVO;

import java.util.List;

/**
 *
 */
public interface MenuService extends IService<Menu> {

    List<UserMenusBackDTO> getUserMenusDTO();

    List<MenusBackDTO> getMenusBackDTO(String keywords);

    void updateMenuStatusVO(StatusBackVO statusBackVO);

    void deleteMenuById(String id);

    void saveOrUpdateMenuBackVO(MenuBackVO menuBackVO);

    List<LabelsBackDTO> getMenusDTO();
}
