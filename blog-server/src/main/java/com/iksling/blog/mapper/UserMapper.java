package com.iksling.blog.mapper;

import com.iksling.blog.dto.UsersOnlineBackDTO;
import com.iksling.blog.dto.UsersBackDTO;
import com.iksling.blog.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.iksling.blog.vo.ConditionBackVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Entity com.iksling.blog.entity.User
 */
public interface UserMapper extends BaseMapper<User> {

    Integer selectUsersBackDTOCount(@Param("condition") ConditionBackVO condition);

    List<UsersBackDTO> selectUsersBackDTO(@Param("condition") ConditionBackVO condition);

    List<UsersOnlineBackDTO> selectUserOnlinesBackDTO(@Param("idList") List<Integer> idList);
}




