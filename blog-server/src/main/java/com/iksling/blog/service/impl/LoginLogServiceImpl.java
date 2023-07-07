package com.iksling.blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iksling.blog.entity.LoginLog;
import com.iksling.blog.service.LoginLogService;
import com.iksling.blog.mapper.LoginLogMapper;
import org.springframework.stereotype.Service;

/**
 *
 */
@Service
public class LoginLogServiceImpl extends ServiceImpl<LoginLogMapper, LoginLog>
    implements LoginLogService{

}




