package com.iksling.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.iksling.blog.dto.LabelBackDTO;
import com.iksling.blog.dto.UserAuthsBackDTO;
import com.iksling.blog.entity.UserAuth;
import com.iksling.blog.pojo.PagePojo;
import com.iksling.blog.vo.PasswordVO;
import com.iksling.blog.vo.StatusBackVO;
import com.iksling.blog.vo.ConditionBackVO;
import com.iksling.blog.vo.UserAuthBackVO;

import java.util.List;

/**
 *
 */
public interface UserAuthService extends IService<UserAuth> {

    void updateUserAuthBackVO(UserAuthBackVO userAuthBackVO);

    void updateUserAuthStatusBackVO(StatusBackVO statusBackVO);

    void updateUserPasswordVO(PasswordVO passwordVO);

    PagePojo<UserAuthsBackDTO> getUserAuthsBackDTO(ConditionBackVO condition);

    List<LabelBackDTO> getBackUsernames(String keywords);
}
