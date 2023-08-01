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
import com.iksling.blog.vo.UpdateBatchVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

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
        if (UserUtil.getLoginUser().getRoleWeight() > 200)
            throw new IllegalRequestException();
        if (UserUtil.getLoginUser().getRoleWeight() > 100 && Objects.equals(condition.getDeletedFlag(), true))
            throw new IllegalRequestException();
        Page<Message> page = new Page<>(condition.getCurrent(), condition.getSize());
        Page<Message> messagePage = messageMapper.selectPage(page, new LambdaQueryWrapper<Message>()
                .select(Message::getId, Message::getUserId, Message::getAvatar, Message::getNickname, Message::getMessageSpeed, Message::getMessageContent, Message::getIpSource, Message::getIpAddress, Message::getCreateTime)
                .eq(Message::getDeletedFlag, condition.getDeletedFlag())
                .like(StringUtils.isNotBlank(condition.getKeywords()), Message::getNickname, condition.getKeywords())
                .orderByDesc(Message::getCreateTime));
        if (messagePage.getTotal() == 0)
            return new PagePojo<>();
        return new PagePojo<>((int) messagePage.getTotal(), BeanCopyUtil.copyList(messagePage.getRecords(), MessagesBackDTO.class));
    }

    @Override
    @Transactional
    public void updateMessagesStatus(UpdateBatchVO updateBatchVO) {
        if (UserUtil.getLoginUser().getRoleWeight() > 200)
            throw new IllegalRequestException();
        int count = messageMapper.updateMessagesStatus(updateBatchVO, UserUtil.getLoginUser().getRoleWeight());
        if (count != updateBatchVO.getIdList().size())
            throw new IllegalRequestException();
    }

    @Override
    @Transactional
    public void deleteMessageIdList(List<Integer> messageIdList) {
        if (UserUtil.getLoginUser().getRoleWeight() > 100 || messageIdList.size() == 0)
            throw new IllegalRequestException();
        int count = messageMapper.deleteBatchIds(messageIdList);
        if (count != messageIdList.size())
            throw new IllegalRequestException();
    }
}




