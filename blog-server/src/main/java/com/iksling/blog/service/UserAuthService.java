package com.iksling.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.iksling.blog.dto.UsernameDTO;
import com.iksling.blog.dto.UsersBackDTO;
import com.iksling.blog.entity.UserAuth;
import com.iksling.blog.pojo.PagePojo;
import com.iksling.blog.vo.ConditionVO;
import com.iksling.blog.vo.UpdateBatchVO;
import com.iksling.blog.vo.UserBackVO;

import java.util.List;

/**
 *
 */
public interface UserAuthService extends IService<UserAuth> {

    List<UsernameDTO> getAllUsername(String keywords);
}
