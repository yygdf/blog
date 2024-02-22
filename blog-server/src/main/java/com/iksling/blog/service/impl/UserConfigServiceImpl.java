package com.iksling.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iksling.blog.dto.UserConfigsBackDTO;
import com.iksling.blog.entity.UserConfig;
import com.iksling.blog.exception.IllegalRequestException;
import com.iksling.blog.mapper.UserConfigMapper;
import com.iksling.blog.pojo.LoginUser;
import com.iksling.blog.pojo.PagePojo;
import com.iksling.blog.service.UserConfigService;
import com.iksling.blog.util.RegexUtil;
import com.iksling.blog.util.UserUtil;
import com.iksling.blog.vo.ConditionBackVO;
import com.iksling.blog.vo.StatusBackVO;
import com.iksling.blog.vo.UserConfigBackVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static com.iksling.blog.constant.CommonConst.ROOT_USER_ID;
import static com.iksling.blog.constant.CommonConst.ROOT_USER_ID_LIST;
import static com.iksling.blog.constant.FlagConst.DELETED;

/**
 *
 */
@Service
public class UserConfigServiceImpl extends ServiceImpl<UserConfigMapper, UserConfig>
    implements UserConfigService{
    private HashMap<String, String> userConfigMap;

    @Autowired
    private UserConfigMapper userConfigMapper;

    @PostConstruct
    public void loadUserConfigMap() {
        userConfigMap = userConfigMapper.selectList(new LambdaQueryWrapper<UserConfig>()
                .select(UserConfig::getConfigName, UserConfig::getConfigValue)
                .eq(UserConfig::getUserId, ROOT_USER_ID))
                .stream()
                .collect(Collectors.toMap(UserConfig::getConfigName, UserConfig::getConfigValue, (key1, key2) -> key2, HashMap::new));
    }

    public HashMap<String, String> getUserConfigMap() {
        return userConfigMap;
    }

    @Override
    @Transactional
    public void updateUserConfigBackVO(UserConfigBackVO userConfigBackVO) {
        LoginUser loginUser = UserUtil.getLoginUser();
        Integer loginUserId = loginUser.getUserId();
        LambdaUpdateWrapper<UserConfig> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        if (userConfigBackVO.getAssimilateFlag() == Boolean.TRUE) {
            if (!loginUserId.equals(ROOT_USER_ID))
                throw new IllegalRequestException();
            lambdaUpdateWrapper.inSql(UserConfig::getConfigName, "select config_name from tb_system_config where id = " + userConfigBackVO.getId());
        } else
            lambdaUpdateWrapper.eq(UserConfig::getId, userConfigBackVO.getId());
        userConfigMapper.update(null, lambdaUpdateWrapper
                .set(userConfigBackVO.getConfigDesc() != null, UserConfig::getConfigDesc, userConfigBackVO.getConfigDesc())
                .set(userConfigBackVO.getConfigValue() != null, UserConfig::getConfigValue, RegexUtil.deleteHTMLTag(userConfigBackVO.getConfigValue()))
                .set(UserConfig::getUpdateUser, loginUserId)
                .set(UserConfig::getUpdateTime, new Date())
                .and(loginUser.getRoleWeight() > 100, e -> e.eq(UserConfig::getDeletedFlag, false)
                            .notIn(UserConfig::getUserId, ROOT_USER_ID_LIST))
                .eq(loginUser.getRoleWeight() > 200, UserConfig::getUserId, loginUserId));
    }

    @Override
    @Transactional
    public void updateUserConfigsStatusBackVO(StatusBackVO statusBackVO) {
        LoginUser loginUser = UserUtil.getLoginUser();
        LambdaUpdateWrapper<UserConfig> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        if (DELETED.equals(statusBackVO.getType())) {
            if (loginUser.getRoleWeight() > 100)
                throw new IllegalRequestException();
            lambdaUpdateWrapper.set(UserConfig::getDeletedFlag, false);
        } else
            lambdaUpdateWrapper.set(UserConfig::getDeletedFlag, true);
        int count = userConfigMapper.update(null, lambdaUpdateWrapper
                .set(UserConfig::getUpdateUser, loginUser.getUserId())
                .set(UserConfig::getUpdateTime, new Date())
                .in(UserConfig::getId, statusBackVO.getIdList())
                .and(loginUser.getRoleWeight() > 100, e -> e.eq(UserConfig::getDeletedFlag, false)
                        .notIn(UserConfig::getUserId, ROOT_USER_ID_LIST))
                .eq(loginUser.getRoleWeight() > 200, UserConfig::getUserId, loginUser.getUserId()));
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




