package com.iksling.blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iksling.blog.dto.ConfigsBackDTO;
import com.iksling.blog.entity.UserConfig;
import com.iksling.blog.pojo.PagePojo;
import com.iksling.blog.service.UserConfigService;
import com.iksling.blog.mapper.UserConfigMapper;
import com.iksling.blog.vo.ConditionVO;
import com.iksling.blog.vo.ConfigBackVO;
import org.springframework.stereotype.Service;

/**
 *
 */
@Service
public class UserConfigServiceImpl extends ServiceImpl<UserConfigMapper, UserConfig>
    implements UserConfigService{

    @Override
    public PagePojo<ConfigsBackDTO> getPageUserConfigsBackDTO(ConditionVO condition) {
        return null;
    }

    @Override
    public void updateUserConfigsStatus(Integer userId, String configName) {

    }

    @Override
    public void deleteUserConfigByConfigName(String configName) {

    }

    @Override
    public void saveOrUpdateUserConfigBackVO(ConfigBackVO configBackVO) {

    }
}




