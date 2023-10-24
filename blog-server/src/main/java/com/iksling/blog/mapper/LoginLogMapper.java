package com.iksling.blog.mapper;

import com.iksling.blog.dto.LoginLogsBackDTO;
import com.iksling.blog.entity.LoginLog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.iksling.blog.vo.ConditionBackVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Entity com.iksling.blog.entity.LoginLog
 */
public interface LoginLogMapper extends BaseMapper<LoginLog> {

    Integer selectLoginLogsBackDTOCount(@Param("condition") ConditionBackVO condition);

    List<LoginLogsBackDTO> selectLoginLogsBackDTO(@Param("condition") ConditionBackVO condition);
}




