package com.iksling.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iksling.blog.dto.UsernameDTO;
import com.iksling.blog.entity.UserAuth;
import com.iksling.blog.service.UserAuthService;
import com.iksling.blog.mapper.UserAuthMapper;
import com.iksling.blog.util.BeanCopyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 */
@Service
public class UserAuthServiceImpl extends ServiceImpl<UserAuthMapper, UserAuth>
    implements UserAuthService{
    @Autowired
    private UserAuthMapper userAuthMapper;

    @Override
    public List<UsernameDTO> getAllUsername() {
        List<UserAuth> userAuthList = userAuthMapper.selectList(new LambdaQueryWrapper<UserAuth>()
                .select(UserAuth::getUserId, UserAuth::getUsername));
        return BeanCopyUtil.copyList(userAuthList, UsernameDTO.class);
    }
}




