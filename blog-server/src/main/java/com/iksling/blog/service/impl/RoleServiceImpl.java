package com.iksling.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iksling.blog.dto.LabelBackDTO;
import com.iksling.blog.dto.LabelsBackDTO;
import com.iksling.blog.dto.RolePermissionBackDTO;
import com.iksling.blog.dto.RolesBackDTO;
import com.iksling.blog.entity.Role;
import com.iksling.blog.entity.RoleMenu;
import com.iksling.blog.entity.RoleResource;
import com.iksling.blog.exception.IllegalRequestException;
import com.iksling.blog.exception.OperationStatusException;
import com.iksling.blog.handler.FilterInvocationSecurityMetadataSourceImpl;
import com.iksling.blog.mapper.RoleMapper;
import com.iksling.blog.mapper.RoleMenuMapper;
import com.iksling.blog.mapper.RoleResourceMapper;
import com.iksling.blog.mapper.UserRoleMapper;
import com.iksling.blog.pojo.LoginUser;
import com.iksling.blog.service.*;
import com.iksling.blog.util.BeanCopyUtil;
import com.iksling.blog.util.UserUtil;
import com.iksling.blog.vo.RoleBackVO;
import com.iksling.blog.vo.RolePermissionBackVO;
import com.iksling.blog.vo.StatusBackVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.iksling.blog.constant.CommonConst.ROOT_ROLE_ID;

/**
 *
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role>
    implements RoleService{
    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;
    @Autowired
    private RoleResourceMapper roleResourceMapper;
    @Autowired
    private RoleMenuMapper roleMenuMapper;

    @Autowired
    private MenuService menuService;
    @Autowired
    private ResourceService resourceService;
    @Autowired
    private RoleMenuService roleMenuService;
    @Autowired
    private RoleResourceService roleResourceService;

    @Autowired
    private SessionRegistry sessionRegistry;
    @Autowired
    private FilterInvocationSecurityMetadataSourceImpl filterInvocationSecurityMetadataSource;

    @Override
    @Transactional
    public void saveOrUpdateRoleBackVO(RoleBackVO roleBackVO) {
        LoginUser loginUser = UserUtil.getLoginUser();
        Role role = BeanCopyUtil.copyObject(roleBackVO, Role.class);
        if (role.getId() == null) {
            if (role.getRoleDesc() == null || role.getRoleName() == null)
                throw new IllegalRequestException();
            Integer count = roleMapper.selectCount(new LambdaQueryWrapper<Role>()
                    .eq(Role::getRoleName, role.getRoleName()));
            if (count > 0)
                throw new OperationStatusException("角色名已存在!");
            role.setUserId(loginUser.getUserId());
            role.setCreateTime(new Date());
            role.setCreateUser(loginUser.getUserId());
            roleMapper.insert(role);
        } else {
            if (role.getDisabledFlag() == Boolean.TRUE) {
                if (role.getId().equals(ROOT_ROLE_ID))
                    throw new IllegalRequestException();
                offlineByRoleId(role.getId());
            }
            if (role.getRoleName() != null) {
                Integer count = roleMapper.selectCount(new LambdaQueryWrapper<Role>()
                        .eq(Role::getRoleName, role.getRoleName()));
                if (count > 0)
                    throw new OperationStatusException("角色名已存在!");
            }
            role.setUpdateTime(new Date());
            role.setUpdateUser(loginUser.getUserId());
            roleMapper.updateById(role);
        }
    }

    @Override
    @Transactional
    public void deleteBackRolesByIdList(List<Integer> idList) {
        if (idList.isEmpty() || idList.contains(ROOT_ROLE_ID))
            throw new IllegalRequestException();
        int count = roleMapper.delete(new LambdaUpdateWrapper<Role>()
                .in(Role::getId, idList)
                .eq(Role::getDeletableFlag, true)
                .notInSql(Role::getId,"select distinct role_id from tb_user_role"));
        if (count != idList.size())
            throw new IllegalRequestException();
        roleMenuMapper.delete(new LambdaUpdateWrapper<RoleMenu>().in(RoleMenu::getRoleId, idList));
        roleResourceMapper.delete(new LambdaUpdateWrapper<RoleResource>().in(RoleResource::getRoleId, idList));
    }

    @Override
    @Transactional
    public void updateRoleStatusBackVO(StatusBackVO statusBackVO) {
        if (statusBackVO.getIdList().contains(ROOT_ROLE_ID))
            throw new IllegalRequestException();
        LambdaUpdateWrapper<Role> lambdaUpdateWrapper = new LambdaUpdateWrapper<Role>()
                .in(Role::getId, statusBackVO.getIdList());
        if (statusBackVO.getStatus() == Boolean.TRUE)
            lambdaUpdateWrapper.set(Role::getDisabledFlag, true);
        else
            lambdaUpdateWrapper.set(Role::getDisabledFlag, false);
        int count = roleMapper.update(null, lambdaUpdateWrapper);
        if (count != statusBackVO.getIdList().size())
            throw new OperationStatusException();
        if (statusBackVO.getStatus() == Boolean.TRUE)
            statusBackVO.getIdList().forEach(this::offlineByRoleId);
    }

    @Override
    @Transactional
    public void updateRolePermissionBackVO(RolePermissionBackVO rolePermissionBackVO) {
        if (rolePermissionBackVO.getMenuIdList() != null) {
            roleMenuService.remove(new LambdaQueryWrapper<RoleMenu>()
                    .eq(RoleMenu::getRoleId, rolePermissionBackVO.getId()));
            roleMenuService.saveBatch(rolePermissionBackVO.getMenuIdList().stream()
                    .map(menuId -> RoleMenu.builder()
                            .roleId(rolePermissionBackVO.getId())
                            .menuId(menuId)
                            .build())
                    .collect(Collectors.toList()));
        }
        if (rolePermissionBackVO.getResourceIdList() != null) {
            roleResourceService.remove(new LambdaQueryWrapper<RoleResource>()
                    .eq(RoleResource::getRoleId, rolePermissionBackVO.getId()));
            roleResourceService.saveBatch(rolePermissionBackVO.getResourceIdList().stream()
                    .map(resourceId -> RoleResource.builder()
                            .roleId(rolePermissionBackVO.getId())
                            .resourceId(resourceId)
                            .build())
                    .collect(Collectors.toList()));
            filterInvocationSecurityMetadataSource.clearResourceRoleList();
        }
    }

    @Override
    public List<RolesBackDTO> getRolesBackDTO(String keywords) {
        return roleMapper.selectRolesBackDTO(keywords);
    }

    @Override
    public List<LabelBackDTO> getBackRoleNames() {
        List<Role> roleList = roleMapper.selectList(new LambdaQueryWrapper<Role>()
                .select(Role::getId, Role::getUserId, Role::getRoleName, Role::getRoleWeight)
                .orderByAsc(Role::getRoleWeight));
        return roleList.stream()
                .map(e -> LabelBackDTO.builder()
                        .id(e.getId())
                        .userId(e.getUserId())
                        .label(e.getRoleName())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public RolePermissionBackDTO getRolePermissionBackDTO() {
        List<LabelsBackDTO> menusRoleDTOList = menuService.getMenusRoleBackDTO();
        List<LabelsBackDTO> resourcesRoleDTOList = resourceService.getResourcesRoleBackDTO();
        return RolePermissionBackDTO.builder()
                .userId(UserUtil.getLoginUser().getUserId())
                .menusRoleDTOList(menusRoleDTOList)
                .resourcesRoleDTOList(resourcesRoleDTOList)
                .build();
    }

    private void offlineByRoleId(Integer roleId) {
        List<Object> loginUserList = sessionRegistry.getAllPrincipals().stream().filter(e -> {
            LoginUser loginUser = (LoginUser) e;
            return loginUser.getRoleIdList().contains(roleId.toString());
        }).collect(Collectors.toList());
        List<SessionInformation> allSessions = new ArrayList<>();
        loginUserList.forEach(e -> allSessions.addAll(sessionRegistry.getAllSessions(e, false)));
        allSessions.forEach(SessionInformation::expireNow);
    }
}




