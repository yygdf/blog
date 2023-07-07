package com.iksling.blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iksling.blog.entity.UserAuth;
import com.iksling.blog.service.UserAuthService;
import com.iksling.blog.mapper.UserAuthMapper;
import org.springframework.stereotype.Service;

/**
 *
 */
@Service
public class UserAuthServiceImpl extends ServiceImpl<UserAuthMapper, UserAuth>
    implements UserAuthService{

}




