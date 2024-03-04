package com.iksling.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iksling.blog.dto.MessagesBackDTO;
import com.iksling.blog.entity.Message;
import com.iksling.blog.exception.IllegalRequestException;
import com.iksling.blog.exception.OperationStatusException;
import com.iksling.blog.mapper.MessageMapper;
import com.iksling.blog.pojo.LoginUser;
import com.iksling.blog.pojo.PagePojo;
import com.iksling.blog.service.MessageService;
import com.iksling.blog.util.BeanCopyUtil;
import com.iksling.blog.util.CommonUtil;
import com.iksling.blog.util.UserUtil;
import com.iksling.blog.pojo.Condition;
import com.iksling.blog.vo.StatusBackVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

import static com.iksling.blog.constant.FlagConst.DELETED;

/**
 *
 */
@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message>
    implements MessageService{
    @Autowired
    private MessageMapper messageMapper;

    @Override
    @Transactional
    public void deleteBackMessagesByIdList(List<Integer> idList) {
        if (idList.isEmpty())
            throw new IllegalRequestException();
        int count = messageMapper.delete(new LambdaUpdateWrapper<Message>()
                .eq(Message::getDeletedFlag, true)
                .in(Message::getId, idList));
        if (count != idList.size())
            throw new IllegalRequestException();
    }

    @Override
    @Transactional
    public void updateMessagesStatusBackVO(StatusBackVO statusBackVO) {
        LoginUser loginUser = UserUtil.getLoginUser();
        LambdaUpdateWrapper<Message> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        if (DELETED.equals(statusBackVO.getType())) {
            if (loginUser.getRoleWeight() > 100)
                throw new IllegalRequestException();
            lambdaUpdateWrapper.set(Message::getDeletedFlag, false);
        } else
            lambdaUpdateWrapper.set(Message::getDeletedFlag, true);
        int count = messageMapper.update(null, lambdaUpdateWrapper
                .eq(loginUser.getRoleWeight() > 100, Message::getDeletedFlag, false)
                .in(Message::getId, statusBackVO.getIdList())
                .set(Message::getUpdateUser, loginUser.getUserId())
                .set(Message::getUpdateTime, new Date()));
        if (count != statusBackVO.getIdList().size())
            throw new OperationStatusException();
    }

    @Override
    public PagePojo<MessagesBackDTO> getMessagesBackDTO(Condition condition) {
        LambdaQueryWrapper<Message> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (DELETED.equals(condition.getType())) {
            if (UserUtil.getLoginUser().getRoleWeight() > 100)
                return new PagePojo<>();
            lambdaQueryWrapper.eq(Message::getDeletedFlag, true);
        } else
            lambdaQueryWrapper.eq(Message::getDeletedFlag, false);
        Page<Message> page = new Page<>(condition.getCurrent(), condition.getSize());
        Page<Message> messagePage = messageMapper.selectPage(page, lambdaQueryWrapper
                .select(Message::getId, Message::getUserId, Message::getAvatar, Message::getNickname, Message::getMessageContent, Message::getIpSource, Message::getIpAddress, Message::getCreateTime)
                .like(CommonUtil.isNotEmpty(condition.getKeywords()), Message::getNickname, condition.getKeywords())
                .orderByDesc(Message::getId));
        if (messagePage.getTotal() == 0)
            return new PagePojo<>();
        return new PagePojo<>((int) messagePage.getTotal(), BeanCopyUtil.copyList(messagePage.getRecords(), MessagesBackDTO.class));
    }
}




