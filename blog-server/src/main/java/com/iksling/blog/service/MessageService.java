package com.iksling.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.iksling.blog.dto.MessagesBackDTO;
import com.iksling.blog.dto.MessagesDTO;
import com.iksling.blog.entity.Message;
import com.iksling.blog.pojo.PagePojo;
import com.iksling.blog.pojo.Condition;
import com.iksling.blog.vo.MessageVO;
import com.iksling.blog.vo.StatusBackVO;

import java.util.List;

/**
 *
 */
public interface MessageService extends IService<Message> {

    void deleteBackMessagesByIdList(List<Integer> idList);

    void updateMessagesStatusBackVO(StatusBackVO statusBackVO);

    PagePojo<MessagesBackDTO> getMessagesBackDTO(Condition condition);

    void saveMessageVO(MessageVO messageVO);

    List<MessagesDTO> getMessagesDTO();
}
