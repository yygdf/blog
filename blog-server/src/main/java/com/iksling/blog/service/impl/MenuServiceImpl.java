package com.iksling.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iksling.blog.dto.LabelsBackDTO;
import com.iksling.blog.dto.MenusBackDTO;
import com.iksling.blog.dto.MenusUserBackDTO;
import com.iksling.blog.entity.Menu;
import com.iksling.blog.entity.RoleMenu;
import com.iksling.blog.exception.IllegalRequestException;
import com.iksling.blog.exception.OperationStatusException;
import com.iksling.blog.mapper.MenuMapper;
import com.iksling.blog.mapper.RoleMenuMapper;
import com.iksling.blog.pojo.LoginUser;
import com.iksling.blog.service.MenuService;
import com.iksling.blog.util.BeanCopyUtil;
import com.iksling.blog.util.CommonUtil;
import com.iksling.blog.util.UserUtil;
import com.iksling.blog.vo.MenuBackVO;
import com.iksling.blog.vo.StatusBackVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.iksling.blog.constant.CommonConst.HOME_MENU_ID;
import static com.iksling.blog.constant.FlagConst.DISABLED;
import static com.iksling.blog.constant.FlagConst.HIDE;

/**
 *
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu>
    implements MenuService{
    @Autowired
    private MenuMapper menuMapper;
    @Autowired
    private RoleMenuMapper roleMenuMapper;

    @Override
    @Transactional
    public void saveOrUpdateMenuBackVO(MenuBackVO menuBackVO) {
        if (HOME_MENU_ID.equals(menuBackVO.getParentId()))
            throw new IllegalRequestException();
        LoginUser loginUser = UserUtil.getLoginUser();
        Menu menu = BeanCopyUtil.copyObject(menuBackVO, Menu.class);
        if (menu.getId() == null) {
            if (menu.getIcon() == null || menu.getPath() == null || menu.getName() == null || menu.getComponent() == null)
                throw new IllegalRequestException();
            Integer count = menuMapper.selectCount(new LambdaQueryWrapper<Menu>()
                    .eq(Menu::getPath, menu.getPath()));
            if (count > 0)
                throw new OperationStatusException("该菜单路径已存在!");
            menu.setUserId(loginUser.getUserId());
            menu.setCreateUser(loginUser.getUserId());
            menu.setCreateTime(new Date());
            menuMapper.insert(menu);
        } else {
            if (menu.getPath() != null) {
                Integer count = menuMapper.selectCount(new LambdaQueryWrapper<Menu>()
                        .eq(Menu::getPath, menu.getPath()));
                if (count > 0)
                    throw new OperationStatusException("该菜单路径已存在!");
            }
            menu.setUpdateUser(loginUser.getUserId());
            menu.setUpdateTime(new Date());
            menuMapper.updateById(menu);
        }
    }

    @Override
    @Transactional
    public void deleteBackMenusByIdList(List<Integer> idList) {
        if (idList.isEmpty())
            throw new IllegalRequestException();
        StringBuilder sb = new StringBuilder();
        idList.forEach(e -> sb.append(e).append(","));
        int count = menuMapper.delete(new LambdaQueryWrapper<Menu>()
                .in(Menu::getId, idList)
                .eq(Menu::getDeletableFlag, true)
                .notExists("select a.id from (select id from tb_menu where parent_id in (" + sb.replace(sb.length() - 1, sb.length(), ")") + ") a"));
        if (count != idList.size())
            throw new IllegalRequestException();
        roleMenuMapper.delete(new LambdaUpdateWrapper<RoleMenu>().in(RoleMenu::getMenuId, idList));
    }

    @Override
    @Transactional
    public void updateMenuStatusBackVO(StatusBackVO statusBackVO) {
        LambdaUpdateWrapper<Menu> lambdaUpdateWrapper = new LambdaUpdateWrapper<Menu>()
                .in(Menu::getId, statusBackVO.getIdList());
        if (HIDE.equals(statusBackVO.getType()))
            lambdaUpdateWrapper.setSql("hide_flag = !hide_flag");
        else if (DISABLED.equals(statusBackVO.getType()))
            lambdaUpdateWrapper.setSql("disabled_flag = !disabled_flag");
        else
            lambdaUpdateWrapper.setSql("hidden_flag = !hidden_flag");
        int count = menuMapper.update(null, lambdaUpdateWrapper);
        if (count != statusBackVO.getIdList().size())
            throw new IllegalRequestException();
    }

    @Override
    public List<MenusBackDTO> getMenusBackDTO(String keywords) {
        List<Menu> menuList = menuMapper.selectList(new LambdaQueryWrapper<Menu>()
                .select(Menu::getId, Menu::getUserId, Menu::getParentId, Menu::getIcon, Menu::getRank, Menu::getPath,
                        Menu::getName, Menu::getComponent, Menu::getHideFlag, Menu::getHiddenFlag, Menu::getDisabledFlag,
                        Menu::getDeletableFlag, Menu::getCreateTime, Menu::getUpdateTime)
                .and(CommonUtil.isNotEmpty(keywords), e -> e.like(Menu::getName, keywords)
                        .or()
                        .inSql(Menu::getId, "select distinct parent_id from tb_menu where name like '%"+keywords+"%'"))
                .orderByAsc(Menu::getRank)
                .orderByAsc(Menu::getId));
        List<Menu> parentMenuList = getParentMenuList(menuList);
        Map<Integer, List<Menu>> childrenMenuMap = getChildrenMenuMap(menuList);
        return convertMenusBackDTOList(parentMenuList, childrenMenuMap);
    }

    @Override
    public List<MenusUserBackDTO> getMenusUserBackDTO() {
        LoginUser loginUser = UserUtil.getLoginUser();
        List<Menu> menuList = menuMapper.selectMenusUserBackDTO(loginUser.getUserId(), loginUser.getRoleWeight());
        List<Menu> parentMenuList = getParentMenuList(menuList);
        Map<Integer, List<Menu>> childrenMenuMap = getChildrenMenuMap(menuList);
        return convertMenusUserBackDTOList(parentMenuList, childrenMenuMap);
    }

    @Override
    public List<LabelsBackDTO> getMenusRoleBackDTO() {
        List<Menu> menuList = menuMapper.selectList(new LambdaQueryWrapper<Menu>()
                .select(Menu::getId, Menu::getUserId, Menu::getParentId, Menu::getName)
                .orderByAsc(Menu::getId));
        List<Menu> parentMenuList = getParentMenuList(menuList);
        Map<Integer, List<Menu>> childrenMenuMap = getChildrenMenuMap(menuList);
        return convertMenusRoleBackDTOList(parentMenuList, childrenMenuMap);
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

    private List<MenusUserBackDTO> convertMenusUserBackDTOList(List<Menu> parentMenuList, Map<Integer, List<Menu>> childrenMenuMap) {
        return parentMenuList.stream()
                .map(parentMenu -> {
                    MenusUserBackDTO menusUserBackDTO = BeanCopyUtil.copyObject(parentMenu, MenusUserBackDTO.class);
                    menusUserBackDTO.setChildren(BeanCopyUtil.copyList(childrenMenuMap.get(parentMenu.getId()), MenusUserBackDTO.class));
                    return menusUserBackDTO;
                })
                .collect(Collectors.toList());
    }

    private List<MenusBackDTO> convertMenusBackDTOList(List<Menu> parentMenuList, Map<Integer, List<Menu>> childrenMenuMap) {
        return parentMenuList.stream()
                .map(parentMenu -> {
                    MenusBackDTO menusBackDTO = BeanCopyUtil.copyObject(parentMenu, MenusBackDTO.class);
                    menusBackDTO.setChildren(BeanCopyUtil.copyList(childrenMenuMap.get(parentMenu.getId()), MenusBackDTO.class));
                    return menusBackDTO;
                })
                .collect(Collectors.toList());
    }

    private List<LabelsBackDTO> convertMenusRoleBackDTOList(List<Menu> parentMenuList, Map<Integer, List<Menu>> childrenMenuMap) {
        return parentMenuList.stream()
                .map(parentMenu -> {
                    LabelsBackDTO labelsBackDTO = LabelsBackDTO.builder()
                            .id(parentMenu.getId())
                            .label(parentMenu.getName())
                            .build();
                    if (childrenMenuMap.get(parentMenu.getId())  != null)
                        labelsBackDTO.setChildren(childrenMenuMap.get(parentMenu.getId()).stream().map(childrenMenu -> LabelsBackDTO.builder()
                                .id(childrenMenu.getId())
                                .label(childrenMenu.getName())
                                .build()).collect(Collectors.toList()));
                    return labelsBackDTO;
                })
                .collect(Collectors.toList());
    }
}




