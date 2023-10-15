package com.iksling.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.iksling.blog.dto.UserOnlinesBackDTO;
import com.iksling.blog.dto.UsersBackDTO;
import com.iksling.blog.entity.User;
import com.iksling.blog.pojo.PagePojo;
import com.iksling.blog.vo.ConditionBackVO;
import com.iksling.blog.vo.UpdateBatchVO;
import com.iksling.blog.vo.UserBackVO;
import com.iksling.blog.vo.UserVO;

import java.util.List;

/**
 *
 */
public interface UserService extends IService<User> {

    PagePojo<UsersBackDTO> getPageUsersBackDTO(ConditionBackVO condition);

    void deleteUserIdList(List<Integer> userIdList);

    void saveOrUpdateUserBackVO(UserBackVO userBackVO);

    boolean getBackUserExistFlag(String keywords);

    void updateUsersStatus(UpdateBatchVO updateBatchVO);

    PagePojo<UserOnlinesBackDTO> getPageUserOnlinesBackDTO(ConditionBackVO condition);

    void deleteUserOnlineIdList(List<Integer> userOnlineIdList);

    void updateUserVO(UserVO userVO);
}
