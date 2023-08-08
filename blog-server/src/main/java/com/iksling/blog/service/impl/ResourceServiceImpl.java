package com.iksling.blog.service.impl;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iksling.blog.dto.ResourcesBackDTO;
import com.iksling.blog.dto.ResourcesBackDTO;
import com.iksling.blog.entity.Resource;
import com.iksling.blog.entity.Resource;
import com.iksling.blog.service.ResourceService;
import com.iksling.blog.mapper.ResourceMapper;
import com.iksling.blog.util.BeanCopyUtil;
import com.iksling.blog.vo.CommonStatusVO;
import com.iksling.blog.vo.ResourceBackVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
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

    }

    @Override
    public void deleteResourceById(String id) {

    }

    @Override
    public void saveOrUpdateResourceBackVO(ResourceBackVO resourceBackVO) {

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
}




