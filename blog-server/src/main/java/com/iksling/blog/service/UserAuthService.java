package com.iksling.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.iksling.blog.dto.LabelDTO;
import com.iksling.blog.entity.UserAuth;
import com.iksling.blog.vo.CommonStatusVO;
import com.iksling.blog.vo.UpdateBatchVO;

import java.util.List;

/**
 *
 */
public interface UserAuthService extends IService<UserAuth> {

    List<LabelDTO> getAllUsername(String keywords);

    void updateUserAuthStatusVO(CommonStatusVO commonStatusVO);

    void updateUserAuthSStatus(UpdateBatchVO updateBatchVO);
}
