package com.iksling.blog.mapper;

import com.iksling.blog.dto.ExceptionLogsBackDTO;
import com.iksling.blog.entity.ExceptionLog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.iksling.blog.pojo.Condition;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Entity com.iksling.blog.entity.ExceptionLog
 */
public interface ExceptionLogMapper extends BaseMapper<ExceptionLog> {

    Integer selectExceptionLogsBackDTOCount(@Param("condition") Condition condition);

    List<ExceptionLogsBackDTO> selectExceptionLogsBackDTO(@Param("condition") Condition condition);
}




