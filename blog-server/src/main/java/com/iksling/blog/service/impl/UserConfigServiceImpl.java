package com.iksling.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iksling.blog.dto.UserConfigsBackDTO;
import com.iksling.blog.entity.UserConfig;
import com.iksling.blog.exception.IllegalRequestException;
import com.iksling.blog.mapper.UserConfigMapper;
import com.iksling.blog.pojo.LoginUser;
import com.iksling.blog.pojo.PagePojo;
import com.iksling.blog.service.UserConfigService;
import com.iksling.blog.util.BeanCopyUtil;
import com.iksling.blog.util.UserUtil;
import com.iksling.blog.vo.ConditionBackVO;
import com.iksling.blog.vo.StatusBackVO;
import com.iksling.blog.vo.UserConfigBackVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

import static com.iksling.blog.constant.CommonConst.ROOT_USER_ID_LIST;
import static com.iksling.blog.constant.FlagConst.DELETED;

/**
 *
 */
@Service
public class UserConfigServiceImpl extends ServiceImpl<UserConfigMapper, UserConfig>
    implements UserConfigService{
    @Autowired
    private UserConfigMapper userConfigMapper;

    @Override
    @Transactional
    public void updateUserConfigBackVO(UserConfigBackVO userConfigBackVO) {
        UserConfig userConfig = BeanCopyUtil.copyObject(userConfigBackVO, UserConfig.class);
        userConfig.setUpdateUser(UserUtil.getLoginUser().getUserId());
        userConfig.setUpdateTime(new Date());
        LoginUser loginUser = UserUtil.getLoginUser();
        userConfigMapper.update(null, new LambdaUpdateWrapper<UserConfig>()
                .set(userConfigBackVO.getConfigDesc() != null, UserConfig::getConfigDesc, userConfigBackVO.getConfigDesc())
                .set(userConfigBackVO.getConfigValue() != null, UserConfig::getConfigValue, userConfigBackVO.getConfigValue())
                .set(UserConfig::getUpdateUser, loginUser.getUserId())
                .set(UserConfig::getUpdateTime, new Date())
                .eq(UserConfig::getId, userConfigBackVO.getId())
                .and(loginUser.getRoleWeight() > 100, e -> e.eq(UserConfig::getDeletedFlag, false)
                            .notIn(UserConfig::getUserId, ROOT_USER_ID_LIST))
                .eq(loginUser.getRoleWeight() > 200, UserConfig::getUserId, loginUser.getUserId()));
    }

    @Override
    @Transactional
    public void updateUserConfigsStatusBackVO(StatusBackVO statusBackVO) {
        LoginUser loginUser = UserUtil.getLoginUser();
        LambdaUpdateWrapper<UserConfig> lambdaUpdateWrapper = new LambdaUpdateWrapper<UserConfig>()
                .in(UserConfig::getId, statusBackVO.getIdList())
                .notIn(loginUser.getRoleWeight() > 100, UserConfig::getUserId, ROOT_USER_ID_LIST);
        if (DELETED.equals(statusBackVO.getType())) {
            if (loginUser.getRoleWeight() > 100)
                throw new IllegalRequestException();
            else
                lambdaUpdateWrapper.set(UserConfig::getDeletedFlag, false);
        } else
            lambdaUpdateWrapper.set(UserConfig::getDeletedFlag, true);
        int count = userConfigMapper.update(null, lambdaUpdateWrapper);
        if (count != statusBackVO.getIdList().size())
            throw new IllegalRequestException();
    }

    @Override
    public PagePojo<UserConfigsBackDTO> getUserConfigsBackDTO(ConditionBackVO condition) {
        LoginUser loginUser = UserUtil.getLoginUser();
        if (DELETED.equals(condition.getType()) && loginUser.getRoleWeight() > 100)
            return new PagePojo<>();
        Integer count = userConfigMapper.selectUserConfigsBackDTOCount(condition, loginUser.getUserId(), loginUser.getRoleWeight());
        if (count == 0)
            return new PagePojo<>();
        condition.setCurrent((condition.getCurrent() - 1) * condition.getSize());
        List<UserConfigsBackDTO> userConfigsBackDTOList = userConfigMapper.selectUserConfigsBackDTO(condition, loginUser.getUserId(), loginUser.getRoleWeight());
        return new PagePojo<>(count, userConfigsBackDTOList);
    }
}




