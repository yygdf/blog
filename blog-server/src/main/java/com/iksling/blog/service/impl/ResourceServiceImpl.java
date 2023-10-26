package com.iksling.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iksling.blog.dto.LabelBackDTO;
import com.iksling.blog.dto.LabelsBackDTO;
import com.iksling.blog.dto.ResourcesBackDTO;
import com.iksling.blog.entity.Resource;
import com.iksling.blog.entity.RoleResource;
import com.iksling.blog.exception.IllegalRequestException;
import com.iksling.blog.exception.OperationStatusException;
import com.iksling.blog.handler.FilterInvocationSecurityMetadataSourceImpl;
import com.iksling.blog.mapper.ResourceMapper;
import com.iksling.blog.mapper.RoleResourceMapper;
import com.iksling.blog.pojo.LoginUser;
import com.iksling.blog.service.ResourceService;
import com.iksling.blog.util.BeanCopyUtil;
import com.iksling.blog.util.UserUtil;
import com.iksling.blog.vo.ResourceBackVO;
import com.iksling.blog.vo.StatusBackVO;
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
public class ResourceServiceImpl extends ServiceImpl<ResourceMapper, Resource>
    implements ResourceService{
    @Autowired
    private ResourceMapper resourceMapper;
    @Autowired
    private RoleResourceMapper roleResourceMapper;

    @Autowired
    private FilterInvocationSecurityMetadataSourceImpl filterInvocationSecurityMetadataSource;

    @Override
    @Transactional
    public void saveOrUpdateResourceBackVO(ResourceBackVO resourceBackVO) {
        LoginUser loginUser = UserUtil.getLoginUser();
        Resource resource = BeanCopyUtil.copyObject(resourceBackVO, Resource.class);
        if (Objects.isNull(resource.getId())) {
            if (Objects.nonNull(resource.getParentId())) {
                if (Objects.isNull(resource.getResourceUri()) || Objects.isNull(resource.getResourceRequestMethod()))
                    throw new IllegalRequestException();
                Integer count = resourceMapper.selectCount(new LambdaQueryWrapper<Resource>()
                        .eq(Resource::getResourceUri, resource.getResourceUri())
                        .eq(Resource::getResourceRequestMethod, resource.getResourceRequestMethod()));
                if (count > 0)
                    throw new OperationStatusException("该资源已存在!");
            }
            resource.setUserId(loginUser.getUserId());
            resource.setCreateUser(loginUser.getUserId());
            resource.setCreateTime(new Date());
            resourceMapper.insert(resource);
        } else {
            if (Objects.nonNull(resource.getResourceUri())) {
                if (Objects.nonNull(resource.getResourceRequestMethod())) {
                    Integer count = resourceMapper.selectCount(new LambdaQueryWrapper<Resource>()
                            .eq(Resource::getResourceUri, resource.getResourceUri())
                            .eq(Resource::getResourceRequestMethod, resource.getResourceRequestMethod()));
                    if (count > 0)
                        throw new OperationStatusException("该资源已存在!");
                } else {
                    List<Object> objectList = resourceMapper.selectObjs(new LambdaQueryWrapper<Resource>()
                            .select(Resource::getResourceRequestMethod)
                            .eq(Resource::getId, resource.getId()));
                    Integer count = resourceMapper.selectCount(new LambdaQueryWrapper<Resource>()
                            .eq(Resource::getResourceUri, resource.getResourceUri())
                            .eq(Resource::getResourceRequestMethod, objectList.get(0)));
                    if (count > 0)
                        throw new OperationStatusException("该资源已存在!");
                }
            } else if (Objects.nonNull(resource.getResourceRequestMethod())) {
                List<Object> objectList = resourceMapper.selectObjs(new LambdaQueryWrapper<Resource>()
                        .select(Resource::getResourceUri)
                        .eq(Resource::getId, resource.getId()));
                Integer count = resourceMapper.selectCount(new LambdaQueryWrapper<Resource>()
                        .eq(Resource::getResourceUri, objectList.get(0))
                        .eq(Resource::getResourceRequestMethod, resource.getResourceRequestMethod()));
                if (count > 0)
                    throw new OperationStatusException("该资源已存在!");
            }
            resource.setUpdateUser(loginUser.getUserId());
            resource.setUpdateTime(new Date());
            resourceMapper.updateById(resource);
        }
        filterInvocationSecurityMetadataSource.clearResourceRoleList();
    }

    @Override
    @Transactional
    public void deleteBackResourcesByIdList(List<Integer> idList) {
        if (CollectionUtils.isEmpty(idList))
            throw new IllegalRequestException();
        StringBuilder sb = new StringBuilder();
        idList.forEach(e -> sb.append(e).append(","));
        int count = resourceMapper.delete(new LambdaQueryWrapper<Resource>()
                .in(Resource::getId, idList)
                .eq(Resource::getDeletableFlag, true)
                .notExists("select a.id from (select id from tb_resource where parent_id in (" + sb.replace(sb.length() - 1, sb.length(), ")") + ") a"));
        if (count != idList.size())
            throw new IllegalRequestException();
        roleResourceMapper.delete(new LambdaUpdateWrapper<RoleResource>().in(RoleResource::getResourceId, idList));
        filterInvocationSecurityMetadataSource.clearResourceRoleList();
    }

    @Override
    @Transactional
    public void updateResourceStatusBackVO(StatusBackVO statusBackVO) {
        LambdaUpdateWrapper<Resource> lambdaUpdateWrapper = new LambdaUpdateWrapper<Resource>()
                .in(Resource::getId, statusBackVO.getIdList());
        if (Objects.equals(statusBackVO.getType(), 10))
            lambdaUpdateWrapper.setSql("anonymous_flag = !anonymous_flag");
        else
            lambdaUpdateWrapper.setSql("disabled_flag = !disabled_flag");
        int count = resourceMapper.update(null, lambdaUpdateWrapper);
        if (count != statusBackVO.getIdList().size())
            throw new IllegalRequestException();
        filterInvocationSecurityMetadataSource.clearResourceRoleList();
    }

    @Override
    public List<ResourcesBackDTO> getResourcesBackDTO(String keywords) {
        List<Resource> resourceList = resourceMapper.selectList(new LambdaQueryWrapper<Resource>()
                .select(Resource::getId, Resource::getUserId, Resource::getParentId, Resource::getResourceUri, Resource::getResourceName, Resource::getResourceRequestMethod,
                        Resource::getDisabledFlag, Resource::getDeletableFlag, Resource::getAnonymousFlag, Resource::getCreateTime, Resource::getUpdateTime)
                .and(StringUtils.isNotBlank(keywords), e -> e.like(Resource::getResourceName, keywords)
                        .or()
                        .inSql(Resource::getId, "select distinct parent_id from tb_resource where resource_name like '%"+keywords+"%'"))
                .orderByAsc(Resource::getId));
        List<Resource> parentResourceList = getParentResourceList(resourceList);
        Map<Integer, List<Resource>> childrenResourceMap = getChildrenResourceMap(resourceList);
        return convertResourcesBackDTOList(parentResourceList, childrenResourceMap);
    }

    @Override
    public List<LabelBackDTO> getBackResourceModuleNames() {
        List<Resource> resourceList = resourceMapper.selectList(new LambdaQueryWrapper<Resource>()
                .select(Resource::getId, Resource::getResourceName)
                .eq(Resource::getParentId, -1)
                .orderByAsc(Resource::getId));
        return resourceList.stream()
                .map(e -> LabelBackDTO.builder()
                        .id(e.getId())
                        .label(e.getResourceName())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public List<LabelsBackDTO> getResourcesRoleBackDTO() {
        List<Resource> resourceList = resourceMapper.selectList(new LambdaQueryWrapper<Resource>()
                .select(Resource::getId, Resource::getUserId, Resource::getParentId, Resource::getResourceName)
                .orderByAsc(Resource::getId));
        List<Resource> parentResourceList = getParentResourceList(resourceList);
        Map<Integer, List<Resource>> childrenResourceMap = getChildrenResourceMap(resourceList);
        return convertResourcesRoleBackDTOList(parentResourceList, childrenResourceMap);
    }

    private List<Resource> getParentResourceList(List<Resource> resourceList) {
        return resourceList.stream()
                .filter(resource -> resource.getParentId() == -1)
                .collect(Collectors.toList());
    }

    private Map<Integer, List<Resource>> getChildrenResourceMap(List<Resource> resourceList) {
        return resourceList.stream()
                .filter(resource -> resource.getParentId() != -1)
                .collect(Collectors.groupingBy(Resource::getParentId));
    }

    private List<ResourcesBackDTO> convertResourcesBackDTOList(List<Resource> parentResourceList, Map<Integer, List<Resource>> childrenResourceMap) {
        return parentResourceList.stream()
                .map(parentResource -> {
                    ResourcesBackDTO resourcesBackDTO = BeanCopyUtil.copyObject(parentResource, ResourcesBackDTO.class);
                    resourcesBackDTO.setChildren(BeanCopyUtil.copyList(childrenResourceMap.get(parentResource.getId()), ResourcesBackDTO.class);
                    return resourcesBackDTO;
                })
                .collect(Collectors.toList());
    }

    private List<LabelsBackDTO> convertResourcesRoleBackDTOList(List<Resource> parentResourceList, Map<Integer, List<Resource>> childrenResourceMap) {
        return parentResourceList.stream()
                .map(parentResource -> {
                    LabelsBackDTO labelsBackDTO = LabelsBackDTO.builder()
                            .id(parentResource.getId())
                            .userId(parentResource.getUserId())
                            .label(parentResource.getResourceName())
                            .build();
                    List<Resource> childrenResourceList = childrenResourceMap.get(parentResource.getId());
                    if (CollectionUtils.isNotEmpty(childrenResourceList)) {
                        List<LabelsBackDTO> labelsBackDTOList = childrenResourceList.stream().map(childrenResource -> LabelsBackDTO.builder()
                                .id(childrenResource.getId())
                                .userId(childrenResource.getUserId())
                                .label(childrenResource.getResourceName())
                                .build()).collect(Collectors.toList());
                        labelsBackDTO.setChildren(labelsBackDTOList);
                    }
                    return labelsBackDTO;
                })
                .collect(Collectors.toList());
    }
}




