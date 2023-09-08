package com.iksling.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iksling.blog.dto.ConfigsBackDTO;
import com.iksling.blog.entity.UserConfig;
import com.iksling.blog.exception.IllegalRequestException;
import com.iksling.blog.mapper.UserConfigMapper;
import com.iksling.blog.pojo.LoginUser;
import com.iksling.blog.pojo.PagePojo;
import com.iksling.blog.service.UserConfigService;
import com.iksling.blog.util.BeanCopyUtil;
import com.iksling.blog.util.UserUtil;
import com.iksling.blog.vo.ConditionVO;
import com.iksling.blog.vo.ConfigBackVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Objects;

/**
 *
 */
@Service
public class UserConfigServiceImpl extends ServiceImpl<UserConfigMapper, UserConfig>
    implements UserConfigService{
    @Autowired
    private UserConfigMapper userConfigMapper;

    @Override
    public PagePojo<ConfigsBackDTO> getPageUserConfigsBackDTO(ConditionVO condition) {
        LoginUser loginUser = UserUtil.getLoginUser();
        Page<UserConfig> page = new Page<>(condition.getCurrent(), condition.getSize());
        Page<UserConfig> userConfigPage = userConfigMapper.selectPage(page, new LambdaQueryWrapper<UserConfig>()
                .select(UserConfig::getId, UserConfig::getUserId, UserConfig::getConfigDesc, UserConfig::getConfigValue, UserConfig::getConfigName, UserConfig::getDeletableFlag, UserConfig::getCreateTime, UserConfig::getUpdateTime)
                .eq(Objects.nonNull(condition.getUserId()), UserConfig::getUserId, condition.getUserId())
                .eq(loginUser.getRoleWeight() > 100, UserConfig::getUserId, loginUser.getId())
                .and(StringUtils.isNotBlank(condition.getKeywords()), e -> e.like(UserConfig::getConfigName, condition.getKeywords())
                        .or()
                        .like(UserConfig::getConfigDesc, condition.getKeywords()))
                .orderByDesc(UserConfig::getId));
        if (userConfigPage.getTotal() == 0)
            return new PagePojo<>();
        return new PagePojo<>((int) userConfigPage.getTotal(), BeanCopyUtil.copyList(userConfigPage.getRecords(), ConfigsBackDTO.class));
    }

    @Override
    public void updateUserConfigsStatus(Integer userId, String configName) {

    }

    @Override
    public void deleteUserConfigByConfigName(String configName) {

    }

    @Override
    public void saveOrUpdateUserConfigBackVO(ConfigBackVO configBackVO) {
        LoginUser loginUser = UserUtil.getLoginUser();
        UserConfig userConfig = UserConfig.builder()
                .id(configBackVO.getId())
                .configDesc(configBackVO.getConfigDesc().trim())
                .configValue(configBackVO.getConfigValue().trim())
                .build();
        if (Objects.isNull(configBackVO.getId())) {
            if (loginUser.getRoleWeight() > 100)
                throw new IllegalRequestException();
            userConfig.setUserId(loginUser.getId());
            userConfig.setConfigName(configBackVO.getConfigName().trim());
            userConfig.setCreateUser(loginUser.getId());
            userConfig.setCreateTime(new Date());
            userConfigMapper.insert(userConfig);
        } else {
            if (loginUser.getRoleWeight() > 100 && Objects.nonNull(configBackVO.getAssimilateFlag()) && configBackVO.getAssimilateFlag())
                throw new IllegalRequestException();
            if (Objects.nonNull(configBackVO.getAssimilateFlag()) && configBackVO.getAssimilateFlag()) {
                userConfigMapper.update(null, new LambdaUpdateWrapper<UserConfig>()
                        .set(UserConfig::getConfigDesc, userConfig.getConfigDesc())
                        .set(UserConfig::getConfigValue, userConfig.getConfigValue())
                        .set(UserConfig::getUpdateUser, loginUser.getId())
                        .set(UserConfig::getUpdateTime, new Date())
                        .eq(UserConfig::getConfigName, configBackVO.getConfigName().trim()));
            }
            else {
                int count = userConfigMapper.update(null, new LambdaUpdateWrapper<UserConfig>()
                        .set(UserConfig::getConfigDesc, userConfig.getConfigDesc())
                        .set(UserConfig::getConfigValue, userConfig.getConfigValue())
                        .set(UserConfig::getUpdateUser, loginUser.getId())
                        .set(UserConfig::getUpdateTime, new Date())
                        .eq(UserConfig::getId, configBackVO.getId())
                        .eq(loginUser.getRoleWeight() > 100, UserConfig::getUserId, loginUser.getId()));
                if (count != 1)
                    throw new IllegalRequestException();
            }
        }
    }
}




