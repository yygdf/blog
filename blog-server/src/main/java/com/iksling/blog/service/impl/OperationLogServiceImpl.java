package com.iksling.blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iksling.blog.entity.OperationLog;
import com.iksling.blog.service.OperationLogService;
import com.iksling.blog.mapper.OperationLogMapper;
import org.springframework.stereotype.Service;

/**
 *
 */
@Service
public class OperationLogServiceImpl extends ServiceImpl<OperationLogMapper, OperationLog>
    implements OperationLogService{

}




