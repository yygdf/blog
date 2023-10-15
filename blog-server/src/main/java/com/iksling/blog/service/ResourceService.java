package com.iksling.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.iksling.blog.dto.LabelBackDTO;
import com.iksling.blog.dto.ResourcesBackDTO;
import com.iksling.blog.dto.LabelsBackDTO;
import com.iksling.blog.entity.Resource;
import com.iksling.blog.vo.StatusBackVO;
import com.iksling.blog.vo.ResourceBackVO;

import java.util.List;

/**
 *
 */
public interface ResourceService extends IService<Resource> {

    List<ResourcesBackDTO> getResourcesBackDTO(String keywords);

    void updateResourceStatusVO(StatusBackVO statusBackVO);

    void deleteResourceById(String id);

    void saveOrUpdateResourceBackVO(ResourceBackVO resourceBackVO);

    List<LabelsBackDTO> getResourcesDTO();

    List<LabelBackDTO> getBackResourceModuleNames();
}
