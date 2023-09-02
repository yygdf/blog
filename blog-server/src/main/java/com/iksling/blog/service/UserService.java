package com.iksling.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.iksling.blog.dto.UserOnlinesBackDTO;
import com.iksling.blog.dto.UsersBackDTO;
import com.iksling.blog.entity.User;
import com.iksling.blog.pojo.PagePojo;
import com.iksling.blog.vo.ConditionVO;
import com.iksling.blog.vo.UserBackVO;

import java.util.List;

/**
 *
 */
public interface UserService extends IService<User> {

    PagePojo<UsersBackDTO> getPageUsersBackDTO(ConditionVO condition);

    void deleteUserIdList(List<Integer> userIdList);

    void saveOrUpdateUserBackVO(UserBackVO userBackVO);

    boolean getBackUserExistFlag(String keywords);

    PagePojo<UserOnlinesBackDTO> getPageUserOnlinesBackDTO(ConditionVO condition);

    void deleteUserOnlineIdList(List<Integer> userOnlineIdList);
}
