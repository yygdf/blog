package com.iksling.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.iksling.blog.dto.UserConfigsBackDTO;
import com.iksling.blog.entity.UserConfig;
import com.iksling.blog.pojo.PagePojo;
import com.iksling.blog.vo.ConditionBackVO;
import com.iksling.blog.vo.StatusBackVO;
import com.iksling.blog.vo.UserConfigBackVO;

import java.util.HashMap;

/**
 *
 */
public interface UserConfigService extends IService<UserConfig> {

    void updateUserConfigBackVO(UserConfigBackVO userConfigBackVO);

    void updateUserConfigsStatusBackVO(StatusBackVO statusBackVO);

    PagePojo<UserConfigsBackDTO> getUserConfigsBackDTO(ConditionBackVO condition);

    HashMap<String, Object> getUserConfigs(Integer userId);
}
