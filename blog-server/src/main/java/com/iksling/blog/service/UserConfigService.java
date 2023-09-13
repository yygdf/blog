package com.iksling.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.iksling.blog.dto.ConfigsBackDTO;
import com.iksling.blog.entity.UserConfig;
import com.iksling.blog.pojo.PagePojo;
import com.iksling.blog.vo.ConditionVO;
import com.iksling.blog.vo.ConfigBackVO;
import com.iksling.blog.vo.UpdateBatchVO;

/**
 *
 */
public interface UserConfigService extends IService<UserConfig> {

    PagePojo<ConfigsBackDTO> getPageUserConfigsBackDTO(ConditionVO condition);

    void deleteUserConfigByConfigName(String configName);

    void saveOrUpdateUserConfigBackVO(ConfigBackVO configBackVO);

    void updateUserConfigsStatus(UpdateBatchVO updateBatchVO);
}
