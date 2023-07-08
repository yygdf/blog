package com.iksling.blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iksling.blog.entity.Message;
import com.iksling.blog.service.MessageService;
import com.iksling.blog.mapper.MessageMapper;
import org.springframework.stereotype.Service;

/**
 *
 */
@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message>
    implements MessageService{

}




