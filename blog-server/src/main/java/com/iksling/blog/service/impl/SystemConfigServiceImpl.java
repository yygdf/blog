package com.iksling.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iksling.blog.dto.ConfigsBackDTO;
import com.iksling.blog.entity.SystemConfig;
import com.iksling.blog.mapper.SystemConfigMapper;
import com.iksling.blog.pojo.LoginUser;
import com.iksling.blog.pojo.PagePojo;
import com.iksling.blog.service.SystemConfigService;
import com.iksling.blog.util.BeanCopyUtil;
import com.iksling.blog.util.UserUtil;
import com.iksling.blog.vo.ConditionVO;
import com.iksling.blog.vo.ConfigBackVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Objects;

/**
 *
 */
@Service
public class SystemConfigServiceImpl extends ServiceImpl<SystemConfigMapper, SystemConfig>
    implements SystemConfigService{
    @Autowired
    private SystemConfigMapper systemConfigMapper;

    @Override
    public PagePojo<ConfigsBackDTO> getPageSystemConfigsBackDTO(ConditionVO condition) {
        Page<SystemConfig> page = new Page<>(condition.getCurrent(), condition.getSize());
        Page<SystemConfig> systemConfigPage = systemConfigMapper.selectPage(page, new LambdaQueryWrapper<SystemConfig>()
                .select(SystemConfig::getId, SystemConfig::getUserId, SystemConfig::getConfigDesc, SystemConfig::getConfigValue, SystemConfig::getConfigName, SystemConfig::getDeletableFlag, SystemConfig::getCreateTime, SystemConfig::getUpdateTime)
                .like(StringUtils.isNotBlank(condition.getKeywords()), SystemConfig::getConfigName, condition.getKeywords())
                .orderByDesc(SystemConfig::getId));
        if (systemConfigPage.getTotal() == 0)
            return new PagePojo<>();
        return new PagePojo<>((int) systemConfigPage.getTotal(), BeanCopyUtil.copyList(systemConfigPage.getRecords(), ConfigsBackDTO.class));
    }

    @Override
    @Transactional
    public void saveOrUpdateSystemConfigBackVO(ConfigBackVO configBackVO) {
        LoginUser loginUser = UserUtil.getLoginUser();
        SystemConfig systemConfig = SystemConfig.builder()
                .id(configBackVO.getId())
                .userId(loginUser.getUserId())
                .configDesc(configBackVO.getConfigDesc().trim())
                .configValue(configBackVO.getConfigValue().trim())
                .build();
        if (Objects.isNull(configBackVO.getId())) {
            systemConfig.setConfigName(configBackVO.getConfigName().trim());
            systemConfig.setCreateUser(loginUser.getUserId());
            systemConfig.setCreateTime(new Date());
        } else {
            systemConfig.setUpdateUser(loginUser.getUserId());
            systemConfig.setUpdateTime(new Date());
        }
        this.saveOrUpdate(systemConfig);
    }
}




