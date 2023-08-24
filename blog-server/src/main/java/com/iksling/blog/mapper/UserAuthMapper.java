package com.iksling.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.iksling.blog.entity.UserAuth;
import com.iksling.blog.vo.UpdateBatchVO;

/**
 * @Entity com.iksling.blog.entity.UserAuth
 */
public interface UserAuthMapper extends BaseMapper<UserAuth> {

    Integer updateUserAuthSStatus(UpdateBatchVO updateBatchVO);
}




