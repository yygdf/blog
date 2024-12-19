package com.iksling.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iksling.blog.dto.LabelsBackDTO;
import com.iksling.blog.dto.ResourcesBackDTO;
import com.iksling.blog.entity.Resource;
import com.iksling.blog.entity.RoleResource;
import com.iksling.blog.exception.IllegalRequestException;
import com.iksling.blog.exception.OperationStatusException;
import com.iksling.blog.mapper.ResourceMapper;
import com.iksling.blog.mapper.RoleResourceMapper;
import com.iksling.blog.service.ResourceService;
import com.iksling.blog.util.*;
import com.iksling.blog.vo.ResourceBackVO;
import com.iksling.blog.vo.StatusBackVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.iksling.blog.constant.FlagConst.ANONYMOUS;

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

    @Override
    @Transactional
    public void saveOrUpdateResourceBackVO(ResourceBackVO resourceBackVO) {
        Integer loginUserId = UserUtil.getLoginUser().getUserId();
        Resource resource = BeanCopyUtil.copyObject(resourceBackVO, Resource.class);
        if (resource.getId() == null) {
            if (resource.getResourceName() == null || resource.getResourceNameEn() == null)
                throw new IllegalRequestException();
            if (resource.getParentId() == null) {
                Integer count = resourceMapper.selectCount(new LambdaQueryWrapper<Resource>()
                        .eq(Resource::getResourceName, resource.getResourceName())
                        .eq(Resource::getParentId, -1));
                if (count > 0)
                    throw new OperationStatusException(LocaleUtil.getMessage("S0013"));
            } else {
                if (resource.getResourceRequestMethod() == null)
                    resource.setResourceRequestMethod("GET");
                if (resource.getResourceUri() == null)
                    throw new IllegalRequestException();
                Integer count = resourceMapper.selectCount(new LambdaQueryWrapper<Resource>()
                        .eq(Resource::getResourceUri, resource.getResourceUri())
                        .eq(Resource::getResourceRequestMethod, resource.getResourceRequestMethod()));
                if (count > 0)
                    throw new OperationStatusException(LocaleUtil.getMessage("S0014"));
            }
            resource.setUserId(loginUserId);
            resource.setCreateUser(loginUserId);
            resource.setCreateTime(new Date());
            resourceMapper.insert(resource);
        } else {
            Resource resourceOrigin = resourceMapper.selectOne(new LambdaQueryWrapper<Resource>()
                    .select(Resource::getParentId, Resource::getResourceUri, Resource::getResourceRequestMethod)
                    .eq(Resource::getId, resource.getId()));
            if (resourceOrigin == null)
                throw new IllegalRequestException();
            if (resourceOrigin.getParentId() == -1) {
                if (resource.getResourceName() == null)
                    throw new IllegalRequestException();
                Integer count = resourceMapper.selectCount(new LambdaQueryWrapper<Resource>()
                        .eq(Resource::getResourceName, resource.getResourceName())
                        .eq(Resource::getParentId, -1));
                if (count > 0)
                    throw new OperationStatusException(LocaleUtil.getMessage("S0013"));
            } else {
                boolean flag = false;
                if (resource.getResourceUri() != null) {
                    resourceOrigin.setResourceUri(resource.getResourceUri());
                    flag = true;
                }
                if (resource.getResourceRequestMethod() != null) {
                    resourceOrigin.setResourceRequestMethod(resource.getResourceRequestMethod());
                    flag = true;
                }
                if (flag) {
                    Integer count = resourceMapper.selectCount(new LambdaQueryWrapper<Resource>()
                            .eq(Resource::getResourceUri, resourceOrigin.getResourceUri())
                            .eq(Resource::getResourceRequestMethod, resourceOrigin.getResourceRequestMethod()));
                    if (count > 0)
                        throw new OperationStatusException(LocaleUtil.getMessage("S0014"));
                }
            }
            resource.setUpdateUser(loginUserId);
            resource.setUpdateTime(new Date());
            resourceMapper.updateById(resource);
        }
        RabbitUtil.sendConfig("FLAG_0", "");
    }

    @Override
    @Transactional
    public void deleteBackResourcesByIdList(List<Integer> idList) {
        if (idList.isEmpty())
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
        RabbitUtil.sendConfig("FLAG_0", "");
    }

    @Override
    @Transactional
    public void updateResourceStatusBackVO(StatusBackVO statusBackVO) {
        LambdaUpdateWrapper<Resource> lambdaUpdateWrapper = new LambdaUpdateWrapper<Resource>()
                .in(Resource::getId, statusBackVO.getIdList());
        if (ANONYMOUS.equals(statusBackVO.getType()))
            lambdaUpdateWrapper.setSql("anonymous_flag = !anonymous_flag");
        else
            lambdaUpdateWrapper.setSql("disabled_flag = !disabled_flag");
        int count = resourceMapper.update(null, lambdaUpdateWrapper);
        if (count != statusBackVO.getIdList().size())
            throw new IllegalRequestException();
        RabbitUtil.sendConfig("FLAG_0", "");
    }

    @Override
    public List<ResourcesBackDTO> getResourcesBackDTO(String keywords) {
        List<Resource> resourceList = resourceMapper.selectList(new LambdaQueryWrapper<Resource>()
                .select(Resource::getId, Resource::getUserId, Resource::getParentId, Resource::getResourceUri,
                        Resource::getResourceName, Resource::getResourceNameEn, Resource::getResourceRequestMethod,
                        Resource::getDisabledFlag, Resource::getDeletableFlag, Resource::getAnonymousFlag, Resource::getCreateTime, Resource::getUpdateTime)
                .and(CommonUtil.isNotEmpty(keywords), e -> e.like(Resource::getResourceName, keywords)
                        .or()
                        .inSql(Resource::getId, "select distinct parent_id from tb_resource where resource_name like '%"+keywords+"%'"))
                .orderByAsc(Resource::getId));
        List<Resource> parentResourceList = getParentResourceList(resourceList);
        Map<Integer, List<Resource>> childrenResourceMap = getChildrenResourceMap(resourceList);
        return convertResourcesBackDTOList(parentResourceList, childrenResourceMap);
    }

    @Override
    public List<LabelsBackDTO> getBackResourceModuleNames() {
        List<Resource> resourceList = resourceMapper.selectList(new LambdaQueryWrapper<Resource>()
                .select(Resource::getId, Resource::getUserId, Resource::getResourceName, Resource::getResourceNameEn)
                .eq(Resource::getParentId, -1)
                .orderByAsc(Resource::getId));
        return resourceList.stream()
                .map(e -> LabelsBackDTO.builder()
                        .id(e.getId())
                        .label(e.getResourceName())
                        .label2(e.getResourceNameEn())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public List<LabelsBackDTO> getResourcesRoleBackDTO() {
        List<Resource> resourceList = resourceMapper.selectList(new LambdaQueryWrapper<Resource>()
                .select(Resource::getId, Resource::getUserId, Resource::getParentId, Resource::getResourceName, Resource::getResourceNameEn)
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
                    resourcesBackDTO.setChildren(BeanCopyUtil.copyList(childrenResourceMap.get(parentResource.getId()), ResourcesBackDTO.class));
                    return resourcesBackDTO;
                })
                .collect(Collectors.toList());
    }

    private List<LabelsBackDTO> convertResourcesRoleBackDTOList(List<Resource> parentResourceList, Map<Integer, List<Resource>> childrenResourceMap) {
        return parentResourceList.stream()
                .map(parentResource -> {
                    LabelsBackDTO labelsBackDTO = LabelsBackDTO.builder()
                            .id(parentResource.getId())
                            .label(parentResource.getResourceName())
                            .label2(parentResource.getResourceNameEn())
                            .build();
                    if (childrenResourceMap.get(parentResource.getId())  != null)
                        labelsBackDTO.setChildren(childrenResourceMap.get(parentResource.getId()).stream().map(childrenResource -> LabelsBackDTO.builder()
                                .id(childrenResource.getId())
                                .label(childrenResource.getResourceName())
                                .label2(childrenResource.getResourceNameEn())
                                .build()).collect(Collectors.toList()));
                    return labelsBackDTO;
                })
                .collect(Collectors.toList());
    }
}




