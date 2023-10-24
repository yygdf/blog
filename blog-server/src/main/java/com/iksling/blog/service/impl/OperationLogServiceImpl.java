package com.iksling.blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iksling.blog.dto.OperationLogsBackDTO;
import com.iksling.blog.entity.OperationLog;
import com.iksling.blog.mapper.OperationLogMapper;
import com.iksling.blog.pojo.PagePojo;
import com.iksling.blog.service.OperationLogService;
import com.iksling.blog.vo.ConditionBackVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 */
@Service
public class OperationLogServiceImpl extends ServiceImpl<OperationLogMapper, OperationLog>
    implements OperationLogService{
    @Autowired
    private OperationLogMapper operationLogMapper;

    @Override
    public PagePojo<OperationLogsBackDTO> getOperationLogsBackDTO(ConditionBackVO condition) {
        Integer count = operationLogMapper.selectOperationLogsBackDTOCount(condition);
        if (count == 0)
            return new PagePojo<>();
        condition.setCurrent((condition.getCurrent() - 1) * condition.getSize());
        List<OperationLogsBackDTO> operationLogsBackDTOList = operationLogMapper.selectOperationLogsBackDTO(condition);
        return new PagePojo<>(count, operationLogsBackDTOList);
    }
}




