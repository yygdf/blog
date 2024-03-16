package com.iksling.blog.listener;

import com.alibaba.fastjson.JSON;
import com.iksling.blog.pojo.Email;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import static com.iksling.blog.constant.MQConst.EMAIL_QUEUE;

@Component
@RabbitListener(queues = EMAIL_QUEUE)
public class EmailQueueListenerImpl {
    @Value("${spring.mail.username}")
    private String ADMIN_EMAIL;

    @Autowired
    private JavaMailSender javaMailSender;

    @RabbitHandler
    public void process(byte[] data) {
        Email email = JSON.parseObject(new String(data), Email.class);
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(ADMIN_EMAIL);
        message.setTo(email.getEmail());
        message.setSubject(email.getSubject());
        message.setText(email.getContent());
        javaMailSender.send(message);
    }
}
