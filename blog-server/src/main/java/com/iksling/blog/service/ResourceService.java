package com.iksling.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.iksling.blog.dto.LabelDTO;
import com.iksling.blog.dto.ResourcesBackDTO;
import com.iksling.blog.dto.LabelsDTO;
import com.iksling.blog.entity.Resource;
import com.iksling.blog.vo.CommonStatusVO;
import com.iksling.blog.vo.ResourceBackVO;

import java.util.List;

/**
 *
 */
public interface ResourceService extends IService<Resource> {

    List<ResourcesBackDTO> getResourcesBackDTO(String keywords);

    void updateResourceStatusVO(CommonStatusVO commonStatusVO);

    void deleteResourceById(String id);

    void saveOrUpdateResourceBackVO(ResourceBackVO resourceBackVO);

    List<LabelsDTO> getResourcesDTO();

    List<LabelDTO> getBackModuleNames();
}
