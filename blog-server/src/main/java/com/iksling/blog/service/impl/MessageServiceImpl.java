package com.iksling.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iksling.blog.dto.MessagesBackDTO;
import com.iksling.blog.dto.MessagesDTO;
import com.iksling.blog.entity.Message;
import com.iksling.blog.exception.IllegalRequestException;
import com.iksling.blog.exception.OperationStatusException;
import com.iksling.blog.mapper.MessageMapper;
import com.iksling.blog.pojo.Condition;
import com.iksling.blog.pojo.LoginUser;
import com.iksling.blog.pojo.PagePojo;
import com.iksling.blog.service.MessageService;
import com.iksling.blog.util.IpUtil;
import com.iksling.blog.util.RegexUtil;
import com.iksling.blog.util.UserUtil;
import com.iksling.blog.vo.MessageVO;
import com.iksling.blog.vo.StatusBackVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
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

    @Resource
    private HttpServletRequest request;

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
        LoginUser loginUser = UserUtil.getLoginUser();
        if (DELETED.equals(condition.getType()) && loginUser.getRoleWeight() > 100)
                return new PagePojo<>();
        Integer count = messageMapper.selectMessagesBackDTOCount(condition);
        if (count == 0)
            return new PagePojo<>();
        condition.setCurrent((condition.getCurrent() - 1) * condition.getSize());
        List<MessagesBackDTO> messagesBackDTOList = messageMapper.selectMessagesBackDTO(condition);
        return new PagePojo<>(count, messagesBackDTOList);
    }

    @Override
    @Transactional
    public void saveMessageVO(MessageVO messageVO) {
        Message message = new Message();
        Integer loginUserId = UserUtil.getLoginUser().getUserId();
        if (loginUserId != -1) {
            message.setUserId(loginUserId);
            message.setCreateUser(loginUserId);
        }
        message.setMessageSpeed(messageVO.getMessageSpeed());
        message.setMessageContent(RegexUtil.deleteHTMLTag(messageVO.getMessageContent()));
        message.setIpAddress(IpUtil.getIpAddress(request));
        message.setIpSource(IpUtil.getIpSource(message.getIpAddress()));
        message.setCreateTime(new Date());
        messageMapper.insert(message);
    }

    @Override
    public List<MessagesDTO> getMessagesDTO() {
        return messageMapper.selectMessagesDTO();
    }
}




