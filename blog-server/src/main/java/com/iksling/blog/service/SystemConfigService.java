package com.iksling.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.iksling.blog.dto.SystemConfigsBackDTO;
import com.iksling.blog.entity.SystemConfig;
import com.iksling.blog.pojo.PagePojo;
import com.iksling.blog.vo.ConditionBackVO;
import com.iksling.blog.vo.SystemConfigBackVO;

import java.util.List;

/**
 *
 */
public interface SystemConfigService extends IService<SystemConfig> {

    void saveOrUpdateSystemConfigBackVO(SystemConfigBackVO systemConfigBackVO);

    void deleteBackSystemConfigsByIdList(List<Integer> idList);

    PagePojo<SystemConfigsBackDTO> getSystemConfigsBackDTO(ConditionBackVO condition);
}
