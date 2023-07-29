package com.iksling.blog.service;

import com.iksling.blog.dto.MessagesBackDTO;
import com.iksling.blog.entity.Message;
import com.baomidou.mybatisplus.extension.service.IService;
import com.iksling.blog.pojo.PagePojo;
import com.iksling.blog.vo.ConditionVO;

import java.util.List;

/**
 *
 */
public interface MessageService extends IService<Message> {

    PagePojo<MessagesBackDTO> getPageMessagesBackDTO(ConditionVO condition);

    void deleteMessageIdList(List<Integer> messageIdList);
}
