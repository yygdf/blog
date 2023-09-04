package com.iksling.blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iksling.blog.dto.SystemConfigsBackDTO;
import com.iksling.blog.entity.SystemConfig;
import com.iksling.blog.mapper.SystemConfigMapper;
import com.iksling.blog.pojo.PagePojo;
import com.iksling.blog.service.SystemConfigService;
import com.iksling.blog.vo.ConditionVO;
import com.iksling.blog.vo.SystemConfigBackVO;
import org.springframework.stereotype.Service;

/**
 *
 */
@Service
public class SystemConfigServiceImpl extends ServiceImpl<SystemConfigMapper, SystemConfig>
    implements SystemConfigService{

    @Override
    public PagePojo<SystemConfigsBackDTO> getPageSystemConfigsBackDTO(ConditionVO condition) {
        return null;
    }

    @Override
    public void saveOrUpdateSystemConfigBackVO(SystemConfigBackVO systemConfigBackVO) {

    }
}




