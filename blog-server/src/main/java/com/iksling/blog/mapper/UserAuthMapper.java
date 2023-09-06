package com.iksling.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.iksling.blog.dto.UserAuthsBackDTO;
import com.iksling.blog.entity.UserAuth;
import com.iksling.blog.vo.ConditionVO;
import com.iksling.blog.vo.UpdateBatchVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Entity com.iksling.blog.entity.UserAuth
 */
public interface UserAuthMapper extends BaseMapper<UserAuth> {

    List<UserAuthsBackDTO> listUserAuthsBackDTO(@Param("condition") ConditionVO condition);

    Integer updateUserAuthsStatus(@Param("updateBatch") UpdateBatchVO updateBatchVO);
}




