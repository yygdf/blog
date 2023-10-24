package com.iksling.blog.mapper;

import com.iksling.blog.dto.OperationLogsBackDTO;
import com.iksling.blog.entity.OperationLog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.iksling.blog.vo.ConditionBackVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Entity com.iksling.blog.entity.OperationLog
 */
public interface OperationLogMapper extends BaseMapper<OperationLog> {

    Integer selectOperationLogsBackDTOCount(@Param("condition") ConditionBackVO condition);

    List<OperationLogsBackDTO> selectOperationLogsBackDTO(@Param("condition") ConditionBackVO condition);
}




