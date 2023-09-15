package com.iksling.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.iksling.blog.dto.LabelDTO;
import com.iksling.blog.dto.UserAuthsBackDTO;
import com.iksling.blog.entity.UserAuth;
import com.iksling.blog.pojo.PagePojo;
import com.iksling.blog.vo.CommonStatusVO;
import com.iksling.blog.vo.ConditionVO;
import com.iksling.blog.vo.UpdateBatchVO;
import com.iksling.blog.vo.UserAuthBackVO;

import java.util.List;

/**
 *
 */
public interface UserAuthService extends IService<UserAuth> {

    List<LabelDTO> getBackUsernames(String keywords);

    void updateUserAuthStatusVO(CommonStatusVO commonStatusVO);

    PagePojo<UserAuthsBackDTO> getPageUserAuthsBackDTO(ConditionVO condition);

    boolean getBackUserAuthExistFlag(String keywords);

    void updateUserAuthBackVO(UserAuthBackVO userAuthBackVO);

    void updateUserAuthsStatus(UpdateBatchVO updateBatchVO);
}
