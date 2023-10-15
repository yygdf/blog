package com.iksling.blog.mapper;

import com.iksling.blog.dto.ExceptionLogsBackDTO;
import com.iksling.blog.entity.ExceptionLog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.iksling.blog.vo.ConditionBackVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Entity com.iksling.blog.entity.ExceptionLog
 */
public interface ExceptionLogMapper extends BaseMapper<ExceptionLog> {

    Integer selectExceptionLogsBackDTOCount(@Param("condition") ConditionBackVO condition);

    List<ExceptionLogsBackDTO> listExceptionLogsBackDTO(@Param("condition") ConditionBackVO condition);
}




