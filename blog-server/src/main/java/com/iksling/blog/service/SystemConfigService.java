package com.iksling.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.iksling.blog.dto.SystemConfigsBackDTO;
import com.iksling.blog.entity.SystemConfig;
import com.iksling.blog.pojo.PagePojo;
import com.iksling.blog.vo.ConditionVO;
import com.iksling.blog.vo.SystemConfigBackVO;

/**
 *
 */
public interface SystemConfigService extends IService<SystemConfig> {

    PagePojo<SystemConfigsBackDTO> getPageSystemConfigsBackDTO(ConditionVO condition);

    void saveOrUpdateSystemConfigBackVO(SystemConfigBackVO systemConfigBackVO);
}
