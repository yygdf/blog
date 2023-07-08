package com.iksling.blog.service.impl;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iksling.blog.dto.UserMenuDTO;
import com.iksling.blog.entity.Menu;
import com.iksling.blog.mapper.MenuMapper;
import com.iksling.blog.service.MenuService;
import com.iksling.blog.util.BeanCopyUtil;
import com.iksling.blog.util.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu>
    implements MenuService{
    @Autowired
    private MenuMapper menuMapper;

    @Override
    public List<UserMenuDTO> listUserMenus() {
        Integer userId = UserUtil.getLoginUser().getUserId();
        List<Menu> menuList = menuMapper.listMenusByUserId(userId);
        List<Menu> parentMenuList = getParentMenuList(menuList);
        Map<Integer, List<Menu>> childrenMenuMap = getChildrenMenuMap(menuList);
        return convertUserMenuDTOList(parentMenuList, childrenMenuMap);
    }

    private List<Menu> getParentMenuList(List<Menu> menuList) {
        return menuList.stream()
                .filter(menu -> menu.getParentId() == -1)
                .collect(Collectors.toList());
    }

    private Map<Integer, List<Menu>> getChildrenMenuMap(List<Menu> menuList) {
        return menuList.stream()
                .filter(menu -> menu.getParentId() != -1)
                .collect(Collectors.groupingBy(Menu::getParentId));
    }

    private List<UserMenuDTO> convertUserMenuDTOList(List<Menu> parentMenuList, Map<Integer, List<Menu>> childrenMenuMap) {
        return parentMenuList.stream()
                .map(parentMenu -> {
                    UserMenuDTO userMenuDTO = BeanCopyUtil.copyObject(parentMenu, UserMenuDTO.class);
                    List<Menu> childrenMenuList = childrenMenuMap.get(parentMenu.getId());
                    if (CollectionUtils.isNotEmpty(childrenMenuList))
                        userMenuDTO.setChildren(BeanCopyUtil.copyList(childrenMenuList, UserMenuDTO.class));
                    return userMenuDTO;
                })
                .collect(Collectors.toList());
    }
}




