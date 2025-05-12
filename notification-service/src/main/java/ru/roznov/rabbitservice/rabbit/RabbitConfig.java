package ru.roznov.rabbitservice.rabbit;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    public static final String EXCHANGE = "worklog.exchange";
    public static final String QUEUE_NOTIFY = "notifications.queue";
    public static final String QUEUE_TELEGRAM = "telegram.queue";
    public static final String ROUTING_KEY_NOTIFY = "notifications.event";
    public static final String ROUTING_KEY_TELEGRAM = "telegram.event";

    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange(EXCHANGE, true, false);
    }

    @Bean
    public Queue notifyQueue() {
        return new Queue(QUEUE_NOTIFY, true);
    }

    @Bean
    public Queue telegramQueue() {
        return new Queue(QUEUE_TELEGRAM, true);
    }

    @Bean
    public Binding notifyBinding() {
        return BindingBuilder
                .bind(notifyQueue())
                .to(topicExchange())
                .with(ROUTING_KEY_NOTIFY);
    }

    @Bean
    public Binding telegramBinding() {
        return BindingBuilder
                .bind(telegramQueue())
                .to(topicExchange())
                .with(ROUTING_KEY_TELEGRAM);
    }

    @Bean
    public AmqpAdmin amqpAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory factory) {
        RabbitTemplate template = new RabbitTemplate(factory);
        template.setMessageConverter(messageConverter());
        return template;
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory factory) {
        var factoryBean = new SimpleRabbitListenerContainerFactory();
        factoryBean.setConnectionFactory(factory);
        factoryBean.setMessageConverter(messageConverter());
        return factoryBean;
    }
}
