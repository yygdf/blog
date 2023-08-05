package com.iksling.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.iksling.blog.dto.UsernameDTO;
import com.iksling.blog.entity.UserAuth;

import java.util.List;

/**
 *
 */
public interface UserAuthService extends IService<UserAuth> {

    List<UsernameDTO> getAllUsername(String keywords);
}
