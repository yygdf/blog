package com.iksling.blog.service;

import com.iksling.blog.dto.LoginLogsBackDTO;
import com.iksling.blog.entity.LoginLog;
import com.baomidou.mybatisplus.extension.service.IService;
import com.iksling.blog.pojo.PagePojo;
import com.iksling.blog.pojo.Condition;

/**
 *
 */
public interface LoginLogService extends IService<LoginLog> {

     PagePojo<LoginLogsBackDTO> getLoginLogsBackDTO(Condition condition);
}
