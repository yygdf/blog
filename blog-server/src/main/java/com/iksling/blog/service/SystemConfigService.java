package com.iksling.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.iksling.blog.dto.ConfigsBackDTO;
import com.iksling.blog.entity.SystemConfig;
import com.iksling.blog.pojo.PagePojo;
import com.iksling.blog.vo.ConditionVO;
import com.iksling.blog.vo.ConfigBackVO;

/**
 *
 */
public interface SystemConfigService extends IService<SystemConfig> {

    PagePojo<ConfigsBackDTO> getPageSystemConfigsBackDTO(ConditionVO condition);

    void saveOrUpdateSystemConfigBackVO(ConfigBackVO configBackVO);
}