package com.iksling.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.iksling.blog.entity.Message;
import com.iksling.blog.vo.UpdateBatchVO;
import org.apache.ibatis.annotations.Param;

/**
 * @Entity com.iksling.blog.entity.Message
 */
public interface MessageMapper extends BaseMapper<Message> {
    Integer updateMessagesStatus(@Param("updateBatch") UpdateBatchVO updateBatch, Integer roleWeight);
}




