package com.iksling.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.iksling.blog.dto.UserAuthsBackDTO;
import com.iksling.blog.entity.UserAuth;
import com.iksling.blog.vo.ConditionBackVO;
import com.iksling.blog.vo.StatusBackVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Entity com.iksling.blog.entity.UserAuth
 */
public interface UserAuthMapper extends BaseMapper<UserAuth> {

    Integer selectUserAuthsBackDTOCount(@Param("condition") ConditionBackVO condition);

    List<UserAuthsBackDTO> selectUserAuthsBackDTO(@Param("condition") ConditionBackVO condition);

    Integer updateUserAuthsStatus(@Param("statusBackVO") StatusBackVO statusBackVO);
}




