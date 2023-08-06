package com.iksling.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iksling.blog.dto.MenusBackDTO;
import com.iksling.blog.dto.UserMenusDTO;
import com.iksling.blog.entity.Menu;
import com.iksling.blog.exception.IllegalRequestException;
import com.iksling.blog.mapper.MenuMapper;
import com.iksling.blog.pojo.LoginUser;
import com.iksling.blog.service.MenuService;
import com.iksling.blog.util.BeanCopyUtil;
import com.iksling.blog.util.UserUtil;
import com.iksling.blog.vo.CommonStatusVO;
import com.iksling.blog.vo.MenuBackVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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
    public List<UserMenusDTO> getUserMenusDTO() {
        LoginUser loginUser = UserUtil.getLoginUser();
        List<Menu> menuList = menuMapper.listMenusByUserId(loginUser.getUserId(), loginUser.getRoleWeight());
        List<Menu> parentMenuList = getParentMenuList(menuList);
        Map<Integer, List<Menu>> childrenMenuMap = getChildrenMenuMap(menuList);
        return convertUserMenuDTOList(parentMenuList, childrenMenuMap);
    }

    @Override
    public List<MenusBackDTO> getMenusBackDTO(String keywords) {
        List<Menu> menuList = menuMapper.listMenusByKeywords(keywords);
        List<Menu> parentMenuList = getParentMenuList(menuList);
        Map<Integer, List<Menu>> childrenMenuMap = getChildrenMenuMap(menuList);
        return convertMenuBackDTOList(parentMenuList, childrenMenuMap);
    }

    @Override
    @Transactional
    public void updateMenuStatusVO(CommonStatusVO commonStatusVO) {
        int count = menuMapper.update(null, new LambdaUpdateWrapper<Menu>()
                .set(Menu::getHideFlag, Objects.isNull(commonStatusVO.getTopFlag()) ? false : commonStatusVO.getTopFlag())
                .set(Menu::getHiddenFlag, commonStatusVO.getHiddenFlag())
                .set(Menu::getDisabledFlag, commonStatusVO.getPublicFlag())
                .eq(Menu::getId, commonStatusVO.getId())
                .eq(UserUtil.getLoginUser().getRoleWeight() > 100, Menu::getDeletableFlag, true));
        if (count != 1)
            throw new IllegalRequestException();
    }

    @Override
    @Transactional
    public void deleteMenuById(String id) {
        try {
            int count = menuMapper.delete(new LambdaQueryWrapper<Menu>()
                    .eq(Menu::getDeletableFlag, true)
                    .and(q -> q.eq(Menu::getId, Integer.parseInt(id)).or().eq(Menu::getParentId, Integer.parseInt(id))));
            if (count != 1)
                throw new IllegalRequestException();
        } catch (NumberFormatException e) {
            throw new IllegalRequestException();
        }
    }

    @Override
    @Transactional
    public void saveOrUpdateMenuBackVO(MenuBackVO menuBackVO) {
        if (Objects.nonNull(menuBackVO.getParentId()) && menuBackVO.getParentId() == 1)
            throw new IllegalRequestException();
        LoginUser loginUser = UserUtil.getLoginUser();
        Menu menu = Menu.builder()
                .id(menuBackVO.getId())
                .parentId(Objects.isNull(menuBackVO.getParentId()) ? -1 : menuBackVO.getParentId())
                .icon(menuBackVO.getIcon().trim())
                .rank(menuBackVO.getRank())
                .path(menuBackVO.getPath().trim())
                .name(menuBackVO.getName().trim())
                .component(menuBackVO.getComponent().trim())
                .hideFlag(menuBackVO.getHideFlag())
                .hiddenFlag(menuBackVO.getHiddenFlag())
                .disabledFlag(menuBackVO.getDisabledFlag())
                .build();
        if (Objects.isNull(menu.getId())) {
            menu.setUserId(loginUser.getUserId());
            menu.setDeletableFlag(true);
            menu.setCreateUser(loginUser.getUserId());
            menu.setCreateTime(new Date());
            menuMapper.insert(menu);
        } else {
            menu.setUpdateUser(loginUser.getUserId());
            menu.setUpdateTime(new Date());
            int count = menuMapper.update(menu, new LambdaUpdateWrapper<Menu>()
                    .eq(Menu::getId, menu.getId())
                    .eq(loginUser.getRoleWeight() > 100, Menu::getDeletableFlag, true));
            if (count != 1)
                throw new IllegalRequestException();
        }
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

    private List<UserMenusDTO> convertUserMenuDTOList(List<Menu> parentMenuList, Map<Integer, List<Menu>> childrenMenuMap) {
        return parentMenuList.stream()
                .map(parentMenu -> {
                    UserMenusDTO userMenusDTO = BeanCopyUtil.copyObject(parentMenu, UserMenusDTO.class);
                    List<Menu> childrenMenuList = childrenMenuMap.get(parentMenu.getId());
                    if (CollectionUtils.isNotEmpty(childrenMenuList))
                        userMenusDTO.setChildren(BeanCopyUtil.copyList(childrenMenuList, UserMenusDTO.class));
                    return userMenusDTO;
                })
                .collect(Collectors.toList());
    }

    private List<MenusBackDTO> convertMenuBackDTOList(List<Menu> parentMenuList, Map<Integer, List<Menu>> childrenMenuMap) {
        return parentMenuList.stream()
                .map(parentMenu -> {
                    MenusBackDTO menusBackDTO = BeanCopyUtil.copyObject(parentMenu, MenusBackDTO.class);
                    List<Menu> childrenMenuList = childrenMenuMap.get(parentMenu.getId());
                    if (CollectionUtils.isNotEmpty(childrenMenuList))
                        menusBackDTO.setChildren(BeanCopyUtil.copyList(childrenMenuList, MenusBackDTO.class));
                    return menusBackDTO;
                })
                .collect(Collectors.toList());
    }
}




