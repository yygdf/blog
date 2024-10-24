package com.iksling.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.iksling.blog.dto.LabelsBackDTO;
import com.iksling.blog.dto.ResourcesBackDTO;
import com.iksling.blog.entity.Resource;
import com.iksling.blog.vo.ResourceBackVO;
import com.iksling.blog.vo.StatusBackVO;

import java.util.List;

/**
 *
 */
public interface ResourceService extends IService<Resource> {

    void saveOrUpdateResourceBackVO(ResourceBackVO resourceBackVO);

    void deleteBackResourcesByIdList(List<Integer> idList);

    void updateResourceStatusBackVO(StatusBackVO statusBackVO);

    List<ResourcesBackDTO> getResourcesBackDTO(String keywords);

    List<LabelsBackDTO> getBackResourceModuleNames();

    List<LabelsBackDTO> getResourcesRoleBackDTO();
}
