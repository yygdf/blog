package com.iksling.blog.mapper;

import com.iksling.blog.dto.SystemConfigsBackDTO;
import com.iksling.blog.entity.SystemConfig;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.iksling.blog.pojo.Condition;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Entity com.iksling.blog.entity.SystemConfig
 */
public interface SystemConfigMapper extends BaseMapper<SystemConfig> {

    Integer selectSystemConfigsBackDTOCount(@Param("condition") Condition condition);

    List<SystemConfigsBackDTO> selectSystemConfigsBackDTO(@Param("condition") Condition condition);
}




