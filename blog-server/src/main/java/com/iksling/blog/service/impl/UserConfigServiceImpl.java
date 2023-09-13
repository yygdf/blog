package com.iksling.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iksling.blog.dto.ConfigsBackDTO;
import com.iksling.blog.entity.UserConfig;
import com.iksling.blog.exception.IllegalRequestException;
import com.iksling.blog.mapper.UserConfigMapper;
import com.iksling.blog.pojo.LoginUser;
import com.iksling.blog.pojo.PagePojo;
import com.iksling.blog.service.UserConfigService;
import com.iksling.blog.util.UserUtil;
import com.iksling.blog.vo.ConditionVO;
import com.iksling.blog.vo.ConfigBackVO;
import com.iksling.blog.vo.UpdateBatchVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static com.iksling.blog.constant.CommonConst.ROOT_USER_ID;
import static com.iksling.blog.constant.CommonConst.ROOT_USER_ID_LIST;

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
        if (loginUser.getRoleWeight() > 100 && Objects.nonNull(condition.getDeletedFlag()) && condition.getDeletedFlag())
            throw new IllegalRequestException();
        condition.setCurrent((condition.getCurrent() - 1) * condition.getSize());
        List<ConfigsBackDTO> configsBackDTOList = userConfigMapper.listUserConfigsBackDTO(condition, loginUser.getUserId(), loginUser.getRoleWeight());
        if (CollectionUtils.isEmpty(configsBackDTOList))
            return new PagePojo<>();
        return new PagePojo<>(configsBackDTOList.size(), configsBackDTOList);
    }

    @Override
    @Transactional
    public void deleteUserConfigByConfigName(String configName) {
        if (UserUtil.getLoginUser().getUserId().equals(ROOT_USER_ID))
            userConfigMapper.deleteByMap(Collections.singletonMap("config_name", configName.trim()));
    }

    @Override
    @Transactional
    public void saveOrUpdateUserConfigBackVO(ConfigBackVO configBackVO) {
        LoginUser loginUser = UserUtil.getLoginUser();
        UserConfig userConfig = UserConfig.builder()
                .id(configBackVO.getId())
                .configDesc(configBackVO.getConfigDesc().trim())
                .configValue(configBackVO.getConfigValue().trim())
                .build();
        if (Objects.isNull(configBackVO.getId())) {
            if (!loginUser.getUserId().equals(ROOT_USER_ID))
                throw new IllegalRequestException();
            userConfig.setUserId(loginUser.getUserId());
            userConfig.setConfigName(configBackVO.getConfigName().trim());
            userConfig.setCreateUser(loginUser.getUserId());
            userConfig.setCreateTime(new Date());
            userConfigMapper.insert(userConfig);
        } else {
            if (loginUser.getUserId().equals(ROOT_USER_ID) && Objects.nonNull(configBackVO.getAssimilateFlag()) && configBackVO.getAssimilateFlag()) {
                userConfigMapper.update(null, new LambdaUpdateWrapper<UserConfig>()
                        .set(UserConfig::getConfigDesc, userConfig.getConfigDesc())
                        .set(UserConfig::getConfigValue, userConfig.getConfigValue())
                        .set(UserConfig::getUpdateUser, loginUser.getUserId())
                        .set(UserConfig::getUpdateTime, new Date())
                        .eq(UserConfig::getConfigName, configBackVO.getConfigName().trim()));
            }
            else {
                int count = userConfigMapper.update(null, new LambdaUpdateWrapper<UserConfig>()
                        .set(UserConfig::getConfigDesc, userConfig.getConfigDesc())
                        .set(UserConfig::getConfigValue, userConfig.getConfigValue())
                        .set(UserConfig::getUpdateUser, loginUser.getUserId())
                        .set(UserConfig::getUpdateTime, new Date())
                        .eq(UserConfig::getId, configBackVO.getId())
                        .eq(loginUser.getRoleWeight() > 200, UserConfig::getUserId, loginUser.getUserId())
                        .notIn(loginUser.getRoleWeight() > 100 && loginUser.getRoleWeight() <= 200, UserConfig::getUserId, ROOT_USER_ID_LIST));
                if (count != 1)
                    throw new IllegalRequestException();
            }
        }
    }

    @Override
    @Transactional
    public void updateUserConfigsStatus(UpdateBatchVO updateBatchVO) {
        LoginUser loginUser = UserUtil.getLoginUser();
        int count = userConfigMapper.updateUserConfigsStatus(updateBatchVO, ROOT_USER_ID_LIST, loginUser.getUserId(), loginUser.getRoleWeight());
        if (count != updateBatchVO.getIdList().size())
            throw new IllegalRequestException();
    }
}




