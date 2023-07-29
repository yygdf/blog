package com.iksling.blog.mapper;

import com.iksling.blog.entity.Message;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Entity com.iksling.blog.entity.Message
 */
public interface MessageMapper extends BaseMapper<Message> {

    Integer updateMessagesDeletedFlag(@Param("messageIdList") List<Integer> messageIdList);
}




