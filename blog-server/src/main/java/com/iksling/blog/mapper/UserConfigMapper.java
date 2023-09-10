package com.iksling.blog.mapper;

import com.iksling.blog.entity.UserConfig;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.iksling.blog.vo.UpdateBatchVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Entity com.iksling.blog.entity.UserConfig
 */
public interface UserConfigMapper extends BaseMapper<UserConfig> {

    Integer updateUserConfigsStatus(@Param("updateBatch") UpdateBatchVO updateBatchVO, @Param("rootUserIdList") List<Integer> rootUserIdList);
}




