package com.iksling.blog.mapper;

import com.iksling.blog.dto.ConfigsBackDTO;
import com.iksling.blog.entity.UserConfig;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.iksling.blog.vo.ConditionBackVO;
import com.iksling.blog.vo.UpdateBatchVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Entity com.iksling.blog.entity.UserConfig
 */
public interface UserConfigMapper extends BaseMapper<UserConfig> {

    int updateUserConfigsStatus(@Param("updateBatch") UpdateBatchVO updateBatchVO, @Param("rootUserIdList") List<Integer> rootUserIdList, Integer userId, Integer roleWeight);

    List<ConfigsBackDTO> listUserConfigsBackDTO(@Param("condition") ConditionBackVO condition, Integer userId, Integer roleWeight);

    Integer selectUserConfigsBackDTOCount(@Param("condition") ConditionBackVO condition, Integer userId, Integer roleWeight);
}




