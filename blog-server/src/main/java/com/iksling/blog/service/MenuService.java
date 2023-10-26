package com.iksling.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.iksling.blog.dto.MenusBackDTO;
import com.iksling.blog.dto.LabelsBackDTO;
import com.iksling.blog.dto.MenusUserBackDTO;
import com.iksling.blog.entity.Menu;
import com.iksling.blog.vo.StatusBackVO;
import com.iksling.blog.vo.MenuBackVO;

import java.util.List;

/**
 *
 */
public interface MenuService extends IService<Menu> {

    void saveOrUpdateMenuBackVO(MenuBackVO menuBackVO);

    void deleteBackMenuByIdList(List<Integer> idList);

    void updateMenuStatusBackVO(StatusBackVO statusBackVO);

    List<MenusUserBackDTO> getMenusUserBackDTO();

    List<MenusBackDTO> getMenusBackDTO(String keywords);

    List<LabelsBackDTO> getMenusRoleBackDTO();
}
