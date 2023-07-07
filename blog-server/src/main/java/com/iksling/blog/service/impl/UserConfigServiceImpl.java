package com.iksling.blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iksling.blog.entity.UserConfig;
import com.iksling.blog.service.UserConfigService;
import com.iksling.blog.mapper.UserConfigMapper;
import org.springframework.stereotype.Service;

/**
 *
 */
@Service
public class UserConfigServiceImpl extends ServiceImpl<UserConfigMapper, UserConfig>
    implements UserConfigService{

}




