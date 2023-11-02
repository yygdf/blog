package com.iksling.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iksling.blog.dto.SystemConfigsBackDTO;
import com.iksling.blog.entity.SystemConfig;
import com.iksling.blog.entity.UserConfig;
import com.iksling.blog.exception.IllegalRequestException;
import com.iksling.blog.exception.OperationStatusException;
import com.iksling.blog.mapper.SystemConfigMapper;
import com.iksling.blog.mapper.UserConfigMapper;
import com.iksling.blog.pojo.LoginUser;
import com.iksling.blog.pojo.PagePojo;
import com.iksling.blog.service.SystemConfigService;
import com.iksling.blog.util.BeanCopyUtil;
import com.iksling.blog.util.UserUtil;
import com.iksling.blog.vo.ConditionBackVO;
import com.iksling.blog.vo.SystemConfigBackVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

import static com.iksling.blog.constant.CommonConst.ROOT_USER_ID;
import static com.iksling.blog.constant.FlagConst.ASSIMILATE;

/**
 *
 */
@Service
public class SystemConfigServiceImpl extends ServiceImpl<SystemConfigMapper, SystemConfig>
    implements SystemConfigService{
    @Autowired
    private SystemConfigMapper systemConfigMapper;

    @Autowired
    private UserConfigMapper userConfigMapper;

    @Override
    @Transactional
    public void saveOrUpdateSystemConfigBackVO(SystemConfigBackVO systemConfigBackVO) {
        LoginUser loginUser = UserUtil.getLoginUser();
        SystemConfig systemConfig = BeanCopyUtil.copyObject(systemConfigBackVO, SystemConfig.class);
        if (systemConfig.getId() == null) {
            if (systemConfig.getConfigName() == null || systemConfig.getConfigValue() == null)
                throw new IllegalRequestException();
            Integer count = systemConfigMapper.selectCount(new LambdaQueryWrapper<SystemConfig>()
                    .eq(SystemConfig::getConfigName, systemConfig.getConfigName()));
            if (count > 0)
                throw new OperationStatusException("该配置已存在!");
            systemConfig.setAssimilateFlag(null);
            systemConfig.setUserId(loginUser.getUserId());
            systemConfig.setCreateUser(loginUser.getUserId());
            systemConfig.setCreateTime(new Date());
            systemConfigMapper.insert(systemConfig);
        } else {
            if (systemConfig.getAssimilateFlag() == Boolean.TRUE && systemConfig.getConfigValue() != null) {
                if (!loginUser.getUserId().equals(ROOT_USER_ID))
                    throw new IllegalRequestException();
                userConfigMapper.update(null, new LambdaUpdateWrapper<UserConfig>()
                        .set(UserConfig::getConfigValue, systemConfig.getConfigValue())
                        .set(UserConfig::getUpdateUser, loginUser.getUserId())
                        .set(UserConfig::getUpdateTime, new Date())
                        .inSql(UserConfig::getConfigName, "select config_name from tb_system_config where id = " + systemConfig.getId()));
            }
            systemConfig.setConfigName(null);
            systemConfig.setUpdateUser(loginUser.getUserId());
            systemConfig.setUpdateTime(new Date());
            systemConfigMapper.updateById(systemConfig);
        }
    }

    @Override
    @Transactional
    public void deleteBackSystemConfigsByIdList(List<Integer> idList) {
        if (idList.isEmpty())
            throw new IllegalRequestException();
        int count = systemConfigMapper.delete(new LambdaUpdateWrapper<SystemConfig>()
                .in(SystemConfig::getId, idList)
                .eq(SystemConfig::getDeletableFlag, true));
        if (count != idList.size())
            throw new IllegalRequestException();
    }

    @Override
    public PagePojo<SystemConfigsBackDTO> getSystemConfigsBackDTO(ConditionBackVO condition) {
        if (ASSIMILATE.equals(condition.getType()) && !UserUtil.getLoginUser().getUserId().equals(ROOT_USER_ID))
            return new PagePojo<>();
        Integer count = systemConfigMapper.selectSystemConfigsBackDTOCount(condition);
        if (count == 0)
            return new PagePojo<>();
        condition.setCurrent((condition.getCurrent() - 1) * condition.getSize());
        List<SystemConfigsBackDTO> systemConfigsBackDTOList = systemConfigMapper.selectSystemConfigsBackDTO(condition);
        return new PagePojo<>(count, systemConfigsBackDTOList);
    }
}




