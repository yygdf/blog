package com.iksling.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.iksling.blog.dto.UserOnlinesBackDTO;
import com.iksling.blog.dto.UsersBackDTO;
import com.iksling.blog.entity.User;
import com.iksling.blog.pojo.PagePojo;
import com.iksling.blog.vo.*;

import java.util.List;

/**
 *
 */
public interface UserService extends IService<User> {

    void saveOrUpdateUserBackVO(UserBackVO userBackVO);

    void deleteBackUserByIdList(List<Integer> idList);

    void updateUsersStatusBackVO(StatusBackVO statusBackVO);

    PagePojo<UsersBackDTO> getUsersBackDTO(ConditionBackVO condition);

    boolean getBackUserExistFlag(String email, String username);

    void deleteBackUserOnlinesByIdList(List<Integer> idList);

    PagePojo<UserOnlinesBackDTO> getUserOnlinesBackDTO(ConditionBackVO condition);

    void updateUserVO(UserVO userVO);
}
