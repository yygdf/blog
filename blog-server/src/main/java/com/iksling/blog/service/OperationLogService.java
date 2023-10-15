package com.iksling.blog.service;

import com.iksling.blog.dto.OperationLogsBackDTO;
import com.iksling.blog.entity.OperationLog;
import com.baomidou.mybatisplus.extension.service.IService;
import com.iksling.blog.pojo.PagePojo;
import com.iksling.blog.vo.ConditionBackVO;

/**
 *
 */
public interface OperationLogService extends IService<OperationLog> {

    PagePojo<OperationLogsBackDTO> getPageOperationLogsBackDTO(ConditionBackVO condition);
}
