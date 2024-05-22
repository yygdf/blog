package com.iksling.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iksling.blog.dto.SystemConfigsBackDTO;
import com.iksling.blog.entity.SystemConfig;
import com.iksling.blog.exception.IllegalRequestException;
import com.iksling.blog.exception.OperationStatusException;
import com.iksling.blog.mapper.SystemConfigMapper;
import com.iksling.blog.pojo.PagePojo;
import com.iksling.blog.service.SystemConfigService;
import com.iksling.blog.util.BeanCopyUtil;
import com.iksling.blog.util.UserUtil;
import com.iksling.blog.pojo.Condition;
import com.iksling.blog.vo.SystemConfigBackVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static com.iksling.blog.constant.CommonConst.*;

/**
 *
 */
@Service
public class SystemConfigServiceImpl extends ServiceImpl<SystemConfigMapper, SystemConfig>
    implements SystemConfigService{

    @Autowired
    private SystemConfigMapper systemConfigMapper;

    @PostConstruct
    public void loadSystemConfigMap() {
        HashMap<String, String> hashMap = systemConfigMapper.selectList(new LambdaQueryWrapper<SystemConfig>()
                .select(SystemConfig::getConfigName, SystemConfig::getConfigValue))
                .stream()
                .collect(Collectors.toMap(SystemConfig::getConfigName, SystemConfig::getConfigValue, (key1, key2) -> key2, HashMap::new));
        ADMIN_CONTACT_QQ = hashMap.get("admin_contact_qq");
        ADMIN_CONTACT_EMAIL = hashMap.get("admin_contact_email");
        WEBSITE_URL = hashMap.get("website_url");
        WEBSITE_URL_BACK = hashMap.get("website_url_back");
        DEFAULT_ROLE_ID = Integer.valueOf(hashMap.get("default_role_id"));
        ROOT_USER_ID = Integer.valueOf(hashMap.get("root_user_id"));
        ROOT_USER_ID_LIST = Arrays.stream(hashMap.get("root_user_id_list").split(",")).map(Integer::parseInt).collect(Collectors.toList());
        HOME_MENU_ID = Integer.valueOf(hashMap.get("home_menu_id"));
        ROOT_ROLE_ID = Integer.valueOf(hashMap.get("root_role_id"));
        ROOT_ROLE_ID_LIST = Arrays.stream(hashMap.get("root_role_id_list").split(",")).map(Integer::parseInt).collect(Collectors.toList());
        DEFAULT_ROLE_ASSIMILATE = Boolean.parseBoolean(hashMap.get("default_role_assimilate"));
        HOME_BLOGGER_ID = Integer.valueOf(hashMap.get("home_blogger_id"));
        ENABLE_USER_CONFIG = Boolean.parseBoolean(hashMap.get("enable_user_config"));
        ENABLE_FILE_TYPE_STRICT = Boolean.parseBoolean(hashMap.get("enable_file_type_strict"));
    }

    @Override
    @Transactional
    public void saveOrUpdateSystemConfigBackVO(SystemConfigBackVO systemConfigBackVO) {
        Integer loginUserId = UserUtil.getLoginUser().getUserId();
        SystemConfig systemConfig = BeanCopyUtil.copyObject(systemConfigBackVO, SystemConfig.class);
        if (systemConfig.getId() == null) {
            if (systemConfig.getConfigName() == null || systemConfig.getConfigValue() == null)
                throw new IllegalRequestException();
            Integer count = systemConfigMapper.selectCount(new LambdaQueryWrapper<SystemConfig>()
                    .eq(SystemConfig::getConfigName, systemConfig.getConfigName()));
            if (count > 0)
                throw new OperationStatusException("该配置已存在!");
            systemConfig.setUserId(loginUserId);
            systemConfig.setCreateUser(loginUserId);
            systemConfig.setCreateTime(new Date());
            systemConfigMapper.insert(systemConfig);
        } else {
            systemConfig.setConfigName(null);
            systemConfig.setUpdateUser(loginUserId);
            systemConfig.setUpdateTime(new Date());
            systemConfigMapper.updateById(systemConfig);
            this.loadSystemConfigMap();
        }
    }

    @Override
    @Transactional
    public void deleteBackSystemConfigsByIdList(List<Integer> idList) {
        if (idList.isEmpty())
            throw new IllegalRequestException();
        int count = systemConfigMapper.delete(new LambdaUpdateWrapper<SystemConfig>()
                .in(SystemConfig::getId, idList)
                .eq(SystemConfig::getDeletableFlag, true));
        if (count != idList.size())
            throw new IllegalRequestException();
    }

    @Override
    public PagePojo<SystemConfigsBackDTO> getSystemConfigsBackDTO(Condition condition) {
        Integer count = systemConfigMapper.selectSystemConfigsBackDTOCount(condition);
        if (count == 0)
            return new PagePojo<>();
        condition.setCurrent((condition.getCurrent() - 1) * condition.getSize());
        List<SystemConfigsBackDTO> systemConfigsBackDTOList = systemConfigMapper.selectSystemConfigsBackDTO(condition);
        return new PagePojo<>(count, systemConfigsBackDTOList);
    }
}




