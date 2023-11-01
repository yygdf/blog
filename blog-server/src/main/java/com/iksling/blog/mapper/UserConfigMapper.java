package com.iksling.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.iksling.blog.dto.UserConfigsBackDTO;
import com.iksling.blog.entity.UserConfig;
import com.iksling.blog.vo.ConditionBackVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Entity com.iksling.blog.entity.UserConfig
 */
public interface UserConfigMapper extends BaseMapper<UserConfig> {

    Integer selectUserConfigsBackDTOCount(@Param("condition") ConditionBackVO condition, Integer userId, Integer roleWeight);

    List<UserConfigsBackDTO> selectUserConfigsBackDTO(@Param("condition") ConditionBackVO condition, Integer userId, Integer roleWeight);
}




