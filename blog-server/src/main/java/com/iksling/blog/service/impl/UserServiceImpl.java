package com.iksling.blog.service.impl;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iksling.blog.dto.UsersBackDTO;
import com.iksling.blog.entity.User;
import com.iksling.blog.exception.IllegalRequestException;
import com.iksling.blog.mapper.UserMapper;
import com.iksling.blog.pojo.PagePojo;
import com.iksling.blog.service.UserService;
import com.iksling.blog.util.UserUtil;
import com.iksling.blog.vo.ConditionVO;
import com.iksling.blog.vo.UpdateBatchVO;
import com.iksling.blog.vo.UserBackVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 *
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{
    @Autowired
    private UserMapper userMapper;

    @Override
    public PagePojo<UsersBackDTO> getPageUsersBackDTO(ConditionVO condition) {
        if (UserUtil.getLoginUser().getRoleWeight() > 100 && Objects.equals(condition.getDeletedFlag(), true))
            throw new IllegalRequestException();
        condition.setCurrent((condition.getCurrent() - 1) * condition.getSize());
        List<UsersBackDTO> usersBackDTOList = userMapper.listUsersBackDTO(condition);
        if (CollectionUtils.isEmpty(usersBackDTOList))
            return new PagePojo<>();
        return new PagePojo<>(usersBackDTOList.size(), usersBackDTOList);
    }

    @Override
    @Transactional
    public void updateUsersStatus(UpdateBatchVO updateBatchVO) {

    }

    @Override
    @Transactional
    public void deleteUserIdList(List<Integer> userIdList) {

    }

    @Override
    @Transactional
    public void saveOrUpdateUserBackVO(UserBackVO userBackVO) {

    }
}




