package com.iksling.blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iksling.blog.dto.ExceptionLogsBackDTO;
import com.iksling.blog.entity.ExceptionLog;
import com.iksling.blog.pojo.PagePojo;
import com.iksling.blog.service.ExceptionLogService;
import com.iksling.blog.mapper.ExceptionLogMapper;
import com.iksling.blog.vo.ConditionBackVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 *
 */
@Service
public class ExceptionLogServiceImpl extends ServiceImpl<ExceptionLogMapper, ExceptionLog>
    implements ExceptionLogService{
    @Autowired
    private ExceptionLogMapper exceptionLogMapper;

    @Override
    public PagePojo<ExceptionLogsBackDTO> getPageExceptionLogsBackDTO(ConditionBackVO condition) {
        if (Objects.nonNull(condition.getKeywords()))
            condition.setKeywords(condition.getKeywords().trim());
        Integer count = exceptionLogMapper.selectExceptionLogsBackDTOCount(condition);
        if (count == 0)
            return new PagePojo<>();
        condition.setCurrent((condition.getCurrent() - 1) * condition.getSize());
        List<ExceptionLogsBackDTO> exceptionLogsBackDTOList = exceptionLogMapper.listExceptionLogsBackDTO(condition);
        return new PagePojo<>(count, exceptionLogsBackDTOList);
    }
}




