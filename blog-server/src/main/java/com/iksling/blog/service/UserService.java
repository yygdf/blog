package com.iksling.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.iksling.blog.dto.UsersOnlineBackDTO;
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

    boolean getBackUserExistFlag(String keywords);

    void deleteBackUsersOnlineByIdList(List<Integer> idList);

    PagePojo<UsersOnlineBackDTO> getUsersOnlineBackDTO(ConditionBackVO condition);

    void updateUserVO(UserVO userVO);
}
