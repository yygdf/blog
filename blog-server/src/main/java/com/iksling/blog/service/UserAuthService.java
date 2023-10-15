package com.iksling.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.iksling.blog.dto.LabelBackDTO;
import com.iksling.blog.dto.UserAuthsBackDTO;
import com.iksling.blog.entity.UserAuth;
import com.iksling.blog.pojo.PagePojo;
import com.iksling.blog.vo.StatusBackVO;
import com.iksling.blog.vo.ConditionBackVO;
import com.iksling.blog.vo.UserAuthBackVO;

import java.util.List;

/**
 *
 */
public interface UserAuthService extends IService<UserAuth> {

    List<LabelBackDTO> getBackUsernames(String keywords);

    void updateUserAuthStatusVO(StatusBackVO statusBackVO);

    PagePojo<UserAuthsBackDTO> getPageUserAuthsBackDTO(ConditionBackVO condition);

    boolean getBackUserAuthExistFlag(String keywords);

    void updateUserAuthBackVO(UserAuthBackVO userAuthBackVO);
}
