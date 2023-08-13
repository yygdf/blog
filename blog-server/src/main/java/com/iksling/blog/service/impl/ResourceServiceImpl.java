package com.iksling.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iksling.blog.dto.ResourcesBackDTO;
import com.iksling.blog.dto.RoleOptionsDTO;
import com.iksling.blog.entity.Resource;
import com.iksling.blog.exception.IllegalRequestException;
import com.iksling.blog.mapper.ResourceMapper;
import com.iksling.blog.pojo.LoginUser;
import com.iksling.blog.service.ResourceService;
import com.iksling.blog.util.BeanCopyUtil;
import com.iksling.blog.util.UserUtil;
import com.iksling.blog.vo.CommonStatusVO;
import com.iksling.blog.vo.ResourceBackVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    @Override
    public List<ResourcesBackDTO> getResourcesBackDTO(String keywords) {
        List<Resource> resourceList = resourceMapper.listResourcesByKeywords(keywords);
        List<Resource> parentResourceList = getParentResourceList(resourceList);
        Map<Integer, List<Resource>> childrenResourceMap = getChildrenResourceMap(resourceList);
        return convertResourceBackDTOList(parentResourceList, childrenResourceMap);
    }

    @Override
    public void updateResourceStatusVO(CommonStatusVO commonStatusVO) {
        int count = resourceMapper.update(null, new LambdaUpdateWrapper<Resource>()
                .set(Resource::getDisabledFlag, commonStatusVO.getHiddenFlag())
                .set(Resource::getAnonymousFlag, commonStatusVO.getPublicFlag())
                .eq(Resource::getId, commonStatusVO.getId())
                .eq(UserUtil.getLoginUser().getRoleWeight() > 100, Resource::getDeletableFlag, true));
        if (count != 1)
            throw new IllegalRequestException();
    }

    @Override
    public void deleteResourceById(String id) {
        try {
            int count = resourceMapper.delete(new LambdaQueryWrapper<Resource>()
                    .eq(Resource::getDeletableFlag, true)
                    .and(q -> q.eq(Resource::getId, Integer.parseInt(id)).or().eq(Resource::getParentId, Integer.parseInt(id))));
            if (count != 1)
                throw new IllegalRequestException();
        } catch (NumberFormatException e) {
            throw new IllegalRequestException();
        }
    }

    @Override
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
                    .eq(Resource::getId, resource.getId())
                    .eq(loginUser.getRoleWeight() > 100, Resource::getDeletableFlag, true));
            if (count != 1)
                throw new IllegalRequestException();
        }
    }

    @Override
    public List<RoleOptionsDTO> getResourcesDTO() {
        List<Resource> resourceList = resourceMapper.selectList(new LambdaQueryWrapper<Resource>()
                .select(Resource::getId, Resource::getUserId, Resource::getParentId, Resource::getResourceName)
                .orderByAsc(Resource::getId));
        List<Resource> parentResourceList = getParentResourceList(resourceList);
        Map<Integer, List<Resource>> childrenResourceMap = getChildrenResourceMap(resourceList);
        return convertResourcesDTOList(parentResourceList, childrenResourceMap);
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

    private List<RoleOptionsDTO> convertResourcesDTOList(List<Resource> parentResourceList, Map<Integer, List<Resource>> childrenResourceMap) {
        return parentResourceList.stream()
                .map(parentResource -> {
                    RoleOptionsDTO roleOptionsDTO = RoleOptionsDTO.builder()
                            .id(parentResource.getId())
                            .userId(parentResource.getUserId())
                            .label(parentResource.getResourceName())
                            .build();
                    List<Resource> childrenResourceList = childrenResourceMap.get(parentResource.getId());
                    if (CollectionUtils.isNotEmpty(childrenResourceList)) {
                        List<RoleOptionsDTO> roleOptionsDTOList = childrenResourceList.stream().map(childrenResource -> RoleOptionsDTO.builder()
                                .id(childrenResource.getId())
                                .userId(childrenResource.getUserId())
                                .label(childrenResource.getResourceName())
                                .build()).collect(Collectors.toList());
                        roleOptionsDTO.setChildren(roleOptionsDTOList);
                    }
                    return roleOptionsDTO;
                })
                .collect(Collectors.toList());
    }
}




