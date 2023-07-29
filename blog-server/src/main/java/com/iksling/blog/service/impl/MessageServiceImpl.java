package com.iksling.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iksling.blog.dto.MessagesBackDTO;
import com.iksling.blog.entity.Message;
import com.iksling.blog.exception.IllegalRequestException;
import com.iksling.blog.mapper.MessageMapper;
import com.iksling.blog.pojo.PagePojo;
import com.iksling.blog.service.MessageService;
import com.iksling.blog.util.BeanCopyUtil;
import com.iksling.blog.util.UserUtil;
import com.iksling.blog.vo.ConditionVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 */
@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message>
    implements MessageService{
    @Autowired
    private MessageMapper messageMapper;

    @Override
    public PagePojo<MessagesBackDTO> getPageMessagesBackDTO(ConditionVO condition) {
        Page<Message> page = new Page<>(condition.getCurrent(), condition.getSize());
        Page<Message> messagePage = messageMapper.selectPage(page, new LambdaQueryWrapper<Message>()
                .select(Message::getId, Message::getUserId, Message::getAvatar, Message::getNickname, Message::getMessageSpeed, Message::getMessageContent, Message::getIpSource, Message::getIpAddress, Message::getCreateTime)
                .eq(Message::getDeletedFlag, false)
                .like(StringUtils.isNotBlank(condition.getKeywords()), Message::getNickname, condition.getKeywords())
                .orderByDesc(Message::getCreateTime));
        if (messagePage.getTotal() == 0)
            return new PagePojo<>();
        return new PagePojo<>((int) messagePage.getTotal(), BeanCopyUtil.copyList(messagePage.getRecords(), MessagesBackDTO.class));
    }

    @Override
    public void deleteMessageIdList(List<Integer> messageIdList) {
        if (UserUtil.getLoginUser().getRoleWeight() > 200)
            throw new IllegalRequestException();
        int count = messageMapper.updateMessagesDeletedFlag(messageIdList);
        if (count != messageIdList.size())
            throw new IllegalRequestException();
    }
}




