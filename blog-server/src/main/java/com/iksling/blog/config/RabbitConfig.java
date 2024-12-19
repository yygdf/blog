package com.iksling.blog.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.iksling.blog.constant.MQConst.*;

@Configuration
public class RabbitConfig {
    @Value("${server.port}")
    private String SERVER_PORT;

    @Bean
    public Queue articleQueue() {
        return new Queue(ARTICLE_QUEUE, true);
    }

    @Bean
    public FanoutExchange maxWellExchange() {
        return new FanoutExchange(ARTICLE_EXCHANGE, true, false);
    }

    @Bean
    public Binding bindingArticleDirect() {
        return BindingBuilder.bind(articleQueue()).to(maxWellExchange());
    }

    @Bean
    public Queue emailQueue() {
        return new Queue(EMAIL_QUEUE, true);
    }

    @Bean
    public FanoutExchange emailExchange() {
        return new FanoutExchange(EMAIL_EXCHANGE, true, false);
    }

    @Bean
    public Binding bindingEmailDirect() {
        return BindingBuilder.bind(emailQueue()).to(emailExchange());
    }

    @Bean
    public Queue configQueue() {
        return new Queue(CONFIG_QUEUE + "_" + SERVER_PORT, true);
    }

    @Bean
    public FanoutExchange configExchange() {
        return new FanoutExchange(CONFIG_EXCHANGE, true, false);
    }

    @Bean
    public Binding bindingConfigDirect() {
        return BindingBuilder.bind(configQueue()).to(configExchange());
    }
}
