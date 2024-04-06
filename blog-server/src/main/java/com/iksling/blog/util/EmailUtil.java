package com.iksling.blog.util;

import com.alibaba.fastjson.JSON;
import com.iksling.blog.pojo.Email;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.iksling.blog.constant.MQConst.EMAIL_EXCHANGE;

@Component
public class EmailUtil {
    private static RabbitTemplate rabbitTemplate;

    @Autowired
    public void setRabbitTemplate(RabbitTemplate rabbitTemplate) {
        EmailUtil.rabbitTemplate = rabbitTemplate;
    }

    public static void sendEmail(String to, String subject, String content) {
        rabbitTemplate.convertAndSend(EMAIL_EXCHANGE, "*", new Message(JSON.toJSONBytes(Email.builder()
                .email(to)
                .subject(subject)
                .content(content)
                .build()), new MessageProperties()));
    }
}
