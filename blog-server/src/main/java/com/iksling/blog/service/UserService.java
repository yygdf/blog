package com.iksling.blog.service;

import com.iksling.blog.dto.UsersBackDTO;
import com.iksling.blog.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.iksling.blog.pojo.PagePojo;
import com.iksling.blog.vo.ConditionVO;
import com.iksling.blog.vo.UpdateBatchVO;
import com.iksling.blog.vo.UserBackVO;

import java.util.List;

/**
 *
 */
public interface UserService extends IService<User> {

    PagePojo<UsersBackDTO> getPageUsersBackDTO(ConditionVO condition);

    void updateUsersStatus(UpdateBatchVO updateBatchVO);

    void deleteUserIdList(List<Integer> userIdList);

    void saveOrUpdateUserBackVO(UserBackVO userBackVO);
}
