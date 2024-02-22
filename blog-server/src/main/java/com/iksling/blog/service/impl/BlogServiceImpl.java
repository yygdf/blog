package com.iksling.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.iksling.blog.entity.UserConfig;
import com.iksling.blog.mapper.UserConfigMapper;
import com.iksling.blog.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.stream.Collectors;

import static com.iksling.blog.constant.CommonConst.ROOT_USER_ID;

/**
 *
 */
@Service
public class BlogServiceImpl implements BlogService {
    @Autowired
    private UserConfigMapper userConfigMapper;

    @Autowired
    private SystemConfigServiceImpl systemConfigServiceImpl;
    @Autowired
    private UserConfigServiceImpl userConfigServiceImpl;

    @Override
    public HashMap<String, Object> getBlogInfo(Integer bloggerId) {
        HashMap<String, Object> hashMap = new HashMap<>();
        HashMap<String, Object> userConfigMap = new HashMap<>();
        if (bloggerId == null) {
            userConfigMap.putAll(userConfigServiceImpl.getUserConfigMap());
            bloggerId = ROOT_USER_ID;
        } else if (bloggerId.equals(ROOT_USER_ID) || !"true".equals(systemConfigServiceImpl.getSystemConfigMap().get("enable_user_config"))) {
            userConfigMap.putAll(userConfigServiceImpl.getUserConfigMap());
        } else {
            userConfigMap = userConfigMapper.selectList(new LambdaQueryWrapper<UserConfig>()
                    .select(UserConfig::getConfigName, UserConfig::getConfigValue)
                    .eq(UserConfig::getUserId, bloggerId)
                    .eq(UserConfig::getDeletedFlag, false))
                    .stream()
                    .collect(Collectors.toMap(UserConfig::getConfigName, UserConfig::getConfigValue, (key1, key2) -> key2, HashMap::new));
            if (userConfigMap.size() == 0)
                userConfigMap.putAll(userConfigServiceImpl.getUserConfigMap());
        }
        hashMap.put("bloggerId", bloggerId);
        hashMap.put("userConfig", userConfigMap);
        return hashMap;
    }
}




