package ru.roznov.rabbitservice.rabbit;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Setter
@RequiredArgsConstructor
public class RabbitConfig {
    @Value("${queue.name.telegram}")
    private String telegramQueueName;
    @Value("${queue.name.notify}")
    private String notificationQueueName;
    @Value("${spring.rabbitmq.username}")
    private String userName;
    @Value("${spring.rabbitmq.password}")
    private String password;
    @Value("${spring.rabbitmq.host}")
    private String host;
    private final RabbitAdmin rabbitAdmin;

    @Bean
    public CachingConnectionFactory connectionFactory() {
        CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory(host);
        cachingConnectionFactory.setUsername(userName);
        cachingConnectionFactory.setPassword(password);
        return cachingConnectionFactory;
    }

    @Bean
    public RabbitAdmin rabbitAdmin() {
        return new RabbitAdmin(connectionFactory());
    }

    @Bean
    public Queue telegramQueue() {
        Queue telegramQueue = new Queue(telegramQueueName, true);
        rabbitAdmin.declareQueue(telegramQueue);
        return telegramQueue;
    }

    @Bean
    public Queue notificationsQueue() {
        Queue notifyQueue = new Queue(notificationQueueName, true);
        rabbitAdmin.declareQueue(notifyQueue);
        return notifyQueue;
    }
}
