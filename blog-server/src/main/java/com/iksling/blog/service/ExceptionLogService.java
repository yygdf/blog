package com.iksling.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.iksling.blog.dto.ExceptionLogsBackDTO;
import com.iksling.blog.entity.ExceptionLog;
import com.iksling.blog.pojo.PagePojo;
import com.iksling.blog.vo.ConditionBackVO;

/**
 *
 */
public interface ExceptionLogService extends IService<ExceptionLog> {

    PagePojo<ExceptionLogsBackDTO> getPageExceptionLogsBackDTO(ConditionBackVO condition);
}
