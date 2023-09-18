package com.iksling.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iksling.blog.dto.LabelDTO;
import com.iksling.blog.dto.LabelsDTO;
import com.iksling.blog.dto.ResourcesBackDTO;
import com.iksling.blog.entity.Resource;
import com.iksling.blog.exception.IllegalRequestException;
import com.iksling.blog.handler.FilterInvocationSecurityMetadataSourceImpl;
import com.iksling.blog.mapper.ResourceMapper;
import com.iksling.blog.mapper.RoleResourceMapper;
import com.iksling.blog.pojo.LoginUser;
import com.iksling.blog.service.ResourceService;
import com.iksling.blog.util.BeanCopyUtil;
import com.iksling.blog.util.UserUtil;
import com.iksling.blog.vo.CommonStatusVO;
import com.iksling.blog.vo.ResourceBackVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
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
    public List<ResourcesBackDTO> getResourcesBackDTO(String keywords) {
        if (Objects.nonNull(keywords))
            keywords = keywords.trim();
        List<Resource> resourceList = resourceMapper.listResourcesByKeywords(keywords);
        List<Resource> parentResourceList = getParentResourceList(resourceList);
        Map<Integer, List<Resource>> childrenResourceMap = getChildrenResourceMap(resourceList);
        return convertResourceBackDTOList(parentResourceList, childrenResourceMap);
    }

    @Override
    @Transactional
    public void updateResourceStatusVO(CommonStatusVO commonStatusVO) {
        int count = resourceMapper.update(null, new LambdaUpdateWrapper<Resource>()
                .set(Resource::getDisabledFlag, commonStatusVO.getHiddenFlag())
                .set(Resource::getAnonymousFlag, commonStatusVO.getPublicFlag())
                .eq(Resource::getId, commonStatusVO.getId()));
        if (count != 1)
            throw new IllegalRequestException();
        filterInvocationSecurityMetadataSource.clearResourceRoleList();
    }

    @Override
    @Transactional
    public void deleteResourceById(String id) {
        try {
            int count = resourceMapper.delete(new LambdaQueryWrapper<Resource>()
                    .eq(Resource::getDeletableFlag, true)
                    .and(q -> q.eq(Resource::getId, Integer.parseInt(id)).or().eq(Resource::getParentId, Integer.parseInt(id))));
            if (count != 1)
                throw new IllegalRequestException();
            roleResourceMapper.deleteByMap(Collections.singletonMap("resource_id", id));
            filterInvocationSecurityMetadataSource.clearResourceRoleList();
        } catch (NumberFormatException e) {
            throw new IllegalRequestException();
        }
    }

    @Override
    @Transactional
    public void saveOrUpdateResourceBackVO(ResourceBackVO resourceBackVO) {
        LoginUser loginUser = UserUtil.getLoginUser();
        Resource resource = Resource.builder()
                .id(resourceBackVO.getId())
                .parentId(resourceBackVO.getParentId())
                .resourceName(resourceBackVO.getResourceName().trim())
                .disabledFlag(resourceBackVO.getDisabledFlag())
                .anonymousFlag(resourceBackVO.getAnonymousFlag())
                .build();
        if (Objects.nonNull(resource.getParentId()) && resource.getParentId() != -1) {
            resource.setResourceUri(resourceBackVO.getResourceUri().trim());
            resource.setResourceRequestMethod(resourceBackVO.getResourceRequestMethod().trim());
        }
        if (Objects.isNull(resource.getId())) {
            resource.setUserId(loginUser.getUserId());
            resource.setCreateUser(loginUser.getUserId());
            resource.setCreateTime(new Date());
            resourceMapper.insert(resource);
        } else {
            resource.setUpdateUser(loginUser.getUserId());
            resource.setUpdateTime(new Date());
            int count = resourceMapper.update(resource, new LambdaUpdateWrapper<Resource>()
                    .eq(Resource::getId, resource.getId()));
            if (count != 1)
                throw new IllegalRequestException();
        }
        filterInvocationSecurityMetadataSource.clearResourceRoleList();
    }

    @Override
    public List<LabelsDTO> getResourcesDTO() {
        List<Resource> resourceList = resourceMapper.selectList(new LambdaQueryWrapper<Resource>()
                .select(Resource::getId, Resource::getUserId, Resource::getParentId, Resource::getResourceName)
                .orderByAsc(Resource::getId));
        List<Resource> parentResourceList = getParentResourceList(resourceList);
        Map<Integer, List<Resource>> childrenResourceMap = getChildrenResourceMap(resourceList);
        return convertResourcesDTOList(parentResourceList, childrenResourceMap);
    }

    @Override
    public List<LabelDTO> getBackResourceModuleNames() {
        List<Resource> resourceList = resourceMapper.selectList(new LambdaQueryWrapper<Resource>()
                .select(Resource::getId, Resource::getResourceName)
                .eq(Resource::getParentId, -1)
                .orderByAsc(Resource::getId));
        return resourceList.stream()
                .map(e -> LabelDTO.builder()
                        .id(e.getId())
                        .label(e.getResourceName())
                        .build())
                .collect(Collectors.toList());
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

    private List<ResourcesBackDTO> convertResourceBackDTOList(List<Resource> parentResourceList, Map<Integer, List<Resource>> childrenResourceMap) {
        return parentResourceList.stream()
                .map(parentResource -> {
                    ResourcesBackDTO resourcesBackDTO = BeanCopyUtil.copyObject(parentResource, ResourcesBackDTO.class);
                    List<Resource> childrenResourceList = childrenResourceMap.get(parentResource.getId());
                    if (CollectionUtils.isNotEmpty(childrenResourceList))
                        resourcesBackDTO.setChildren(BeanCopyUtil.copyList(childrenResourceList, ResourcesBackDTO.class));
                    return resourcesBackDTO;
                })
                .collect(Collectors.toList());
    }

    private List<LabelsDTO> convertResourcesDTOList(List<Resource> parentResourceList, Map<Integer, List<Resource>> childrenResourceMap) {
        return parentResourceList.stream()
                .map(parentResource -> {
                    LabelsDTO labelsDTO = LabelsDTO.builder()
                            .id(parentResource.getId())
                            .userId(parentResource.getUserId())
                            .label(parentResource.getResourceName())
                            .build();
                    List<Resource> childrenResourceList = childrenResourceMap.get(parentResource.getId());
                    if (CollectionUtils.isNotEmpty(childrenResourceList)) {
                        List<LabelsDTO> labelsDTOList = childrenResourceList.stream().map(childrenResource -> LabelsDTO.builder()
                                .id(childrenResource.getId())
                                .userId(childrenResource.getUserId())
                                .label(childrenResource.getResourceName())
                                .build()).collect(Collectors.toList());
                        labelsDTO.setChildren(labelsDTOList);
                    }
                    return labelsDTO;
                })
                .collect(Collectors.toList());
    }
}




