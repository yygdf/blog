package com.iksling.blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iksling.blog.dto.LoginLogsBackDTO;
import com.iksling.blog.entity.LoginLog;
import com.iksling.blog.mapper.LoginLogMapper;
import com.iksling.blog.pojo.PagePojo;
import com.iksling.blog.service.LoginLogService;
import com.iksling.blog.vo.ConditionVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 */
@Service
public class LoginLogServiceImpl extends ServiceImpl<LoginLogMapper, LoginLog>
    implements LoginLogService{
    @Autowired
    private LoginLogMapper loginLogMapper;

    @Override
    public PagePojo<LoginLogsBackDTO> getPageLoginLogsBackDTO(ConditionVO condition) {
        Integer count = loginLogMapper.selectLoginLogsBackDTOCount(condition);
        if (count == 0)
            return new PagePojo<>();
        condition.setCurrent((condition.getCurrent() - 1) * condition.getSize());
        List<LoginLogsBackDTO> loginLogsBackDTOList = loginLogMapper.listLoginLogsBackDTO(condition);
        return new PagePojo<>(count, loginLogsBackDTOList);
    }
}




