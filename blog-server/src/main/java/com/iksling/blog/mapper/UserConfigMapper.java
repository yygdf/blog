package com.iksling.blog.mapper;

import com.iksling.blog.dto.ConfigsBackDTO;
import com.iksling.blog.entity.UserConfig;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.iksling.blog.vo.ConditionVO;
import com.iksling.blog.vo.UpdateBatchVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Entity com.iksling.blog.entity.UserConfig
 */
public interface UserConfigMapper extends BaseMapper<UserConfig> {

    Integer updateUserConfigsStatus(@Param("updateBatch") UpdateBatchVO updateBatchVO, Integer rootUserId);

    List<ConfigsBackDTO> listUserConfigsBackDTO(@Param("condition") ConditionVO condition, Integer userId, Integer roleWeight);
}




