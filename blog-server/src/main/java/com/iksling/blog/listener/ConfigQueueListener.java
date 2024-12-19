package com.iksling.blog.listener;

import com.alibaba.fastjson.JSON;
import com.iksling.blog.handler.FilterInvocationSecurityMetadataSourceImpl;
import com.iksling.blog.service.impl.UserConfigServiceImpl;
import com.iksling.blog.util.RedisUtil;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.Collectors;

import static com.iksling.blog.constant.CommonConst.*;
import static com.iksling.blog.constant.MQConst.CONFIG_QUEUE;
import static com.iksling.blog.constant.RedisConst.USER_MESSAGE_CONFIG;

@Component
@RabbitListener(queues = "#{T(java.lang.String).format('"+CONFIG_QUEUE+"_%d', ${server.port})}")
public class ConfigQueueListener {
    @Autowired
    private FilterInvocationSecurityMetadataSourceImpl filterInvocationSecurityMetadataSource;
    @Autowired
    private UserConfigServiceImpl userConfigServiceImpl;

    @RabbitHandler
    public void process(byte[] data) {
        HashMap<String, String> map = JSON.parseObject(new String(data), HashMap.class);
        map.forEach((k, v) -> {
            switch (k) {
                case "admin_contact_qq":
                    ADMIN_CONTACT_QQ = v;
                    break;
                case "admin_contact_email":
                    ADMIN_CONTACT_EMAIL = v;
                    break;
                case "website_url":
                    WEBSITE_URL = v;
                    break;
                case "website_url_back":
                    WEBSITE_URL_BACK = v;
                    break;
                case "default_role_id":
                    DEFAULT_ROLE_ID = Integer.valueOf(v);
                    break;
                case "root_user_id":
                    ROOT_USER_ID = Integer.valueOf(v);
                    break;
                case "root_user_id_list":
                    ROOT_USER_ID_LIST = Arrays.stream(v.split(",")).map(Integer::parseInt).collect(Collectors.toList());
                    break;
                case "home_menu_id":
                    HOME_MENU_ID = Integer.valueOf(v);
                    break;
                case "root_role_id":
                    ROOT_ROLE_ID = Integer.valueOf(v);
                    break;
                case "root_role_id_list":
                    ROOT_ROLE_ID_LIST = Arrays.stream(v.split(",")).map(Integer::parseInt).collect(Collectors.toList());
                    break;
                case "default_role_assimilate":
                    DEFAULT_ROLE_ASSIMILATE = Boolean.parseBoolean(v);
                    break;
                case "home_blogger_id":
                    HOME_BLOGGER_ID = Integer.valueOf(v);
                    break;
                case "enable_user_config":
                    ENABLE_USER_CONFIG = Boolean.parseBoolean(v);
                    break;
                case "enable_file_type_strict":
                    ENABLE_FILE_TYPE_STRICT = Boolean.parseBoolean(v);
                    break;
                case "permission_modify_offline_user":
                    PERMISSION_MODIFY_OFFLINE_USER = Boolean.parseBoolean(v);
                    break;
                case "disable_mod_pass_user_id_list":
                    DISABLE_MOD_PASS_USER_ID_LIST = Arrays.stream(v.split(",")).map(Integer::parseInt).collect(Collectors.toList());
                    break;
                case "FLAG_0":
                    filterInvocationSecurityMetadataSource.loadResourceRoleList();
                    break;
                case "FLAG_1":
                    userConfigServiceImpl.loadUserConfigMap();
                    break;
                case "FLAG_2":
                    USER_MESSAGE_CONFIG_MAP.put(v, RedisUtil.getMapValue(USER_MESSAGE_CONFIG, v));
                    break;
            }
        });
    }
}
