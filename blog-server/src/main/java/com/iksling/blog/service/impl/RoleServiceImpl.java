package com.iksling.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iksling.blog.dto.LabelDTO;
import com.iksling.blog.dto.LabelsDTO;
import com.iksling.blog.dto.RoleOptionDTO;
import com.iksling.blog.dto.RolesBackDTO;
import com.iksling.blog.entity.Role;
import com.iksling.blog.entity.RoleMenu;
import com.iksling.blog.entity.RoleResource;
import com.iksling.blog.entity.UserRole;
import com.iksling.blog.exception.IllegalRequestException;
import com.iksling.blog.exception.OperationStatusException;
import com.iksling.blog.handler.FilterInvocationSecurityMetadataSourceImpl;
import com.iksling.blog.mapper.RoleMapper;
import com.iksling.blog.mapper.RoleMenuMapper;
import com.iksling.blog.mapper.RoleResourceMapper;
import com.iksling.blog.mapper.UserRoleMapper;
import com.iksling.blog.pojo.LoginUser;
import com.iksling.blog.service.*;
import com.iksling.blog.util.UserUtil;
import com.iksling.blog.vo.CommonStatusVO;
import com.iksling.blog.vo.RoleBackVO;
import com.iksling.blog.vo.RoleOptionVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
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
    public List<RolesBackDTO> getRolesBackDTO(String keywords) {
        if (Objects.nonNull(keywords))
            keywords = keywords.trim();
        return roleMapper.listRolesBackDTO(keywords);
    }

    @Override
    public RoleOptionDTO getRoleOptionDTO() {
        List<LabelsDTO> menusDTOList = menuService.getMenusDTO();
        List<LabelsDTO> resourcesDTOList = resourceService.getResourcesDTO();
        return RoleOptionDTO.builder()
                .userId(UserUtil.getLoginUser().getUserId())
                .menusDTOList(menusDTOList)
                .resourcesDTOList(resourcesDTOList)
                .build();
    }

    @Override
    @Transactional
    public void updateRoleStatusVO(CommonStatusVO commonStatusVO) {
        if (commonStatusVO.getId().equals(ROOT_ROLE_ID))
            throw new IllegalRequestException();
        int count = roleMapper.update(null, new LambdaUpdateWrapper<Role>()
                .set(Role::getDisabledFlag, commonStatusVO.getPublicFlag())
                .eq(Role::getId, commonStatusVO.getId()));
        if (count != 1)
            throw new IllegalRequestException();
        if (commonStatusVO.getPublicFlag()) {
            Role role = roleMapper.selectOne(new LambdaQueryWrapper<Role>()
                    .select(Role::getRoleName)
                    .eq(Role::getId, commonStatusVO.getId()));
            offlineByRoleName(role.getRoleName());
        }
    }

    @Override
    @Transactional
    public void deleteRoleById(String id) {
        try {
            int roleId = Integer.parseInt(id);
            Integer count = userRoleMapper.selectCount(new LambdaQueryWrapper<UserRole>()
                    .eq(UserRole::getRoleId, roleId));
            if (count > 0)
                throw new IllegalRequestException();
            count = roleMapper.delete(new LambdaQueryWrapper<Role>()
                    .eq(Role::getId, roleId)
                    .eq(Role::getDeletableFlag, true));
            if (count != 1)
                throw new IllegalRequestException();
            userRoleMapper.deleteByMap(Collections.singletonMap("role_id", roleId));
            roleMenuMapper.deleteByMap(Collections.singletonMap("role_id", roleId));
            roleResourceMapper.deleteByMap(Collections.singletonMap("role_id", roleId));
        } catch (NumberFormatException e) {
            throw new IllegalRequestException();
        }
    }

    @Override
    @Transactional
    public void saveOrUpdateRoleBackVO(RoleBackVO roleBackVO) {
        if (ROOT_ROLE_ID.equals(roleBackVO.getId()) && (Objects.nonNull(roleBackVO.getDisabledFlag()) && roleBackVO.getDisabledFlag()))
            throw new IllegalRequestException();
        LoginUser loginUser = UserUtil.getLoginUser();
        Role role = Role.builder()
                .id(roleBackVO.getId())
                .roleDesc(roleBackVO.getRoleDesc().trim())
                .roleWeight(roleBackVO.getRoleWeight())
                .disabledFlag(roleBackVO.getDisabledFlag())
                .build();
        if (Objects.isNull(role.getId())) {
            Integer count = roleMapper.selectCount(new LambdaQueryWrapper<Role>()
                    .eq(Role::getRoleName, roleBackVO.getRoleName()));
            if (count > 0)
                throw new OperationStatusException("角色名已存在!");
            role.setRoleName(roleBackVO.getRoleName().trim());
            role.setUserId(loginUser.getUserId());
            role.setCreateTime(new Date());
            role.setCreateUser(loginUser.getUserId());
            roleMapper.insert(role);
        } else {
            role.setUpdateTime(new Date());
            role.setUpdateUser(loginUser.getUserId());
            int count = roleMapper.updateById(role);
            if (count != 1)
                throw new IllegalRequestException();
            if (Objects.nonNull(role.getDisabledFlag()) && role.getDisabledFlag())
                offlineByRoleName(role.getRoleName());
        }
    }

    @Override
    @Transactional
    public void updateRoleOptionVO(RoleOptionVO roleOptionVO) {
        if (Objects.nonNull(roleOptionVO.getMenuIdList())) {
            roleMenuService.remove(new LambdaQueryWrapper<RoleMenu>()
                    .eq(RoleMenu::getRoleId, roleOptionVO.getId()));
            if (CollectionUtils.isNotEmpty(roleOptionVO.getMenuIdList()))
                roleMenuService.saveBatch(roleOptionVO.getMenuIdList().stream()
                        .map(menuId -> RoleMenu.builder()
                                .roleId(roleOptionVO.getId())
                                .menuId(menuId)
                                .build())
                        .collect(Collectors.toList()));
        }
        if (Objects.nonNull(roleOptionVO.getResourceIdList())) {
            roleResourceService.remove(new LambdaQueryWrapper<RoleResource>()
                    .eq(RoleResource::getRoleId, roleOptionVO.getId()));
            if (CollectionUtils.isNotEmpty(roleOptionVO.getResourceIdList()))
                roleResourceService.saveBatch(roleOptionVO.getResourceIdList().stream()
                        .map(resourceId -> RoleResource.builder()
                                .roleId(roleOptionVO.getId())
                                .resourceId(resourceId)
                                .build())
                        .collect(Collectors.toList()));
            filterInvocationSecurityMetadataSource.clearResourceRoleList();
        }
    }

    @Override
    public List<LabelDTO> getBackRoleNames() {
        List<Role> roleList = roleMapper.selectList(new LambdaQueryWrapper<Role>()
                .select(Role::getId, Role::getRoleName, Role::getRoleWeight)
                .orderByAsc(Role::getRoleWeight));
        return roleList.stream()
                .map(e -> LabelDTO.builder()
                        .id(e.getId())
                        .label(e.getRoleName())
                        .build())
                .collect(Collectors.toList());
    }

    private void offlineByRoleName(String roleName) {
        List<Object> loginUserList = sessionRegistry.getAllPrincipals().stream().filter(item -> {
            LoginUser loginUser = (LoginUser) item;
            return loginUser.getRoleList().contains(roleName);
        }).collect(Collectors.toList());
        List<SessionInformation> allSessions = new ArrayList<>();
        loginUserList.forEach(item -> allSessions.addAll(sessionRegistry.getAllSessions(item, false)));
        allSessions.forEach(SessionInformation::expireNow);
    }
}




