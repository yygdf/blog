package com.iksling.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.iksling.blog.dto.UserOnlinesBackDTO;
import com.iksling.blog.dto.UsersBackDTO;
import com.iksling.blog.entity.User;
import com.iksling.blog.pojo.Condition;
import com.iksling.blog.pojo.Dict;
import com.iksling.blog.pojo.PagePojo;
import com.iksling.blog.vo.*;

import java.util.List;

/**
 *
 */
public interface UserService extends IService<User> {

    void saveOrUpdateUserBackVO(UserBackVO userBackVO);

    String saveUserAvatarBackVO(UserAvatarBackVO userAvatarBackVO);

    void deleteBackUserByIdList(List<Integer> idList);

    void deleteBackUserOnlinesByIdList(List<Integer> idList);

    void updateUsersStatusBackVO(StatusBackVO statusBackVO);

    void updateBackUserAvatarsByFileNameList(List<Long> fileNameList);

    void updateUserVO(UserVO userVO);

    String saveUserAvatar(MultiFileVO multiFileVO);

    PagePojo<UsersBackDTO> getUsersBackDTO(Condition condition);

    boolean getBackUserExistFlag(String email, String username);

    PagePojo<UserOnlinesBackDTO> getUserOnlinesBackDTO(Condition condition);

    void saveUserEmailCode(EmailCodeVO emailCodeVO);

    void saveUserRegisterVO(UserRegisterVO userRegisterVO);

    Dict qqLogin(QQOauthVO qqOauthVO);

    void updateUserEmailVO(EmailVO emailVO);
}
