package com.iksling.blog.service;

import com.iksling.blog.dto.OperationLogsBackDTO;
import com.iksling.blog.entity.OperationLog;
import com.baomidou.mybatisplus.extension.service.IService;
import com.iksling.blog.pojo.PagePojo;
import com.iksling.blog.pojo.Condition;

/**
 *
 */
public interface OperationLogService extends IService<OperationLog> {

    PagePojo<OperationLogsBackDTO> getOperationLogsBackDTO(Condition condition);
}
