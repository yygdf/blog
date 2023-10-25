package com.iksling.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iksling.blog.dto.LabelsBackDTO;
import com.iksling.blog.dto.MenusBackDTO;
import com.iksling.blog.dto.MenusUserBackDTO;
import com.iksling.blog.entity.Menu;
import com.iksling.blog.exception.IllegalRequestException;
import com.iksling.blog.exception.OperationStatusException;
import com.iksling.blog.mapper.MenuMapper;
import com.iksling.blog.mapper.RoleMenuMapper;
import com.iksling.blog.pojo.LoginUser;
import com.iksling.blog.service.MenuService;
import com.iksling.blog.util.BeanCopyUtil;
import com.iksling.blog.util.UserUtil;
import com.iksling.blog.vo.MenuBackVO;
import com.iksling.blog.vo.StatusBackVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static com.iksling.blog.constant.CommonConst.HOME_MENU_ID;

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
        if (Objects.nonNull(menuBackVO.getParentId()) && menuBackVO.getParentId().equals(HOME_MENU_ID))
            throw new IllegalRequestException();
        LoginUser loginUser = UserUtil.getLoginUser();
        Menu menu = BeanCopyUtil.copyObject(menuBackVO, Menu.class);
        if (Objects.isNull(menu.getId())) {
            if (Objects.isNull(menu.getIcon()) || Objects.isNull(menu.getPath()) || Objects.isNull(menu.getName()) || Objects.isNull(menu.getComponent()))
                throw new OperationStatusException();
            menu.setUserId(loginUser.getUserId());
            menu.setCreateUser(loginUser.getUserId());
            menu.setCreateTime(new Date());
            menuMapper.insert(menu);
        } else {
            menu.setUpdateUser(loginUser.getUserId());
            menu.setUpdateTime(new Date());
            menuMapper.updateById(menu);
        }
    }

    @Override
    @Transactional
    public void deleteBackMenuById(Integer id) {
        int count = menuMapper.delete(new LambdaQueryWrapper<Menu>()
                .eq(Menu::getId, id)
                .eq(Menu::getDeletableFlag, true)
                .notExists("select id from tb_menu where parent_id = " + id));
        if (count != 1)
            throw new IllegalRequestException();
        roleMenuMapper.deleteByMap(Collections.singletonMap("menu_id", id));
    }

    @Override
    @Transactional
    public void updateMenuStatusBackVO(StatusBackVO statusBackVO) {
        LambdaUpdateWrapper<Menu> lambdaUpdateWrapper = new LambdaUpdateWrapper<Menu>()
                .in(Menu::getId, statusBackVO.getIdList());
        if (Objects.equals(statusBackVO.getType(), 8))
            lambdaUpdateWrapper.setSql("hide_flag = !hide_flag");
        else if (Objects.equals(statusBackVO.getType(), 9))
            lambdaUpdateWrapper.setSql("disabled_flag = !disabled_flag");
        else
            lambdaUpdateWrapper.setSql("hidden_flag = !hidden_flag");
        int count = menuMapper.update(null, lambdaUpdateWrapper);
        if (count != statusBackVO.getIdList().size())
            throw new IllegalRequestException();
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
    public List<MenusBackDTO> getMenusBackDTO(String keywords) {
        List<Menu> menuList = menuMapper.selectList(new LambdaQueryWrapper<Menu>()
                .select(Menu::getId, Menu::getUserId, Menu::getParentId, Menu::getIcon, Menu::getRank, Menu::getPath,
                        Menu::getName, Menu::getComponent, Menu::getHideFlag, Menu::getHiddenFlag, Menu::getDisabledFlag,
                        Menu::getDeletableFlag, Menu::getCreateTime, Menu::getUpdateTime)
                .and(StringUtils.isNotBlank(keywords), e -> e.like(Menu::getName, keywords)
                        .or()
                        .inSql(Menu::getId, "select distinct parent_id from tb_menu where name like '%"+keywords+"%'"))
                .orderByAsc(Menu::getRank)
                .orderByAsc(Menu::getId));
        List<Menu> parentMenuList = getParentMenuList(menuList);
        Map<Integer, List<Menu>> childrenMenuMap = getChildrenMenuMap(menuList);
        return convertMenusBackDTOList(parentMenuList, childrenMenuMap);
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
                            .userId(parentMenu.getUserId())
                            .label(parentMenu.getName())
                            .build();
                    List<Menu> childrenMenuList = childrenMenuMap.get(parentMenu.getId());
                    if (CollectionUtils.isNotEmpty(childrenMenuList)) {
                        List<LabelsBackDTO> labelsBackDTOList = childrenMenuList.stream().map(childrenMenu -> LabelsBackDTO.builder()
                                .id(childrenMenu.getId())
                                .userId(childrenMenu.getUserId())
                                .label(childrenMenu.getName())
                                .build()).collect(Collectors.toList());
                        labelsBackDTO.setChildren(labelsBackDTOList);
                    }
                    return labelsBackDTO;
                })
                .collect(Collectors.toList());
    }
}




