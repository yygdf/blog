package com.iksling.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.iksling.blog.dto.MessagesBackDTO;
import com.iksling.blog.dto.MessagesDTO;
import com.iksling.blog.entity.Message;
import com.iksling.blog.pojo.Condition;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Entity com.iksling.blog.entity.Message
 */
public interface MessageMapper extends BaseMapper<Message> {
    Integer selectMessagesBackDTOCount(@Param("condition") Condition condition);

    List<MessagesBackDTO> selectMessagesBackDTO(@Param("condition") Condition condition);

    List<MessagesDTO> selectMessagesDTO();
}




