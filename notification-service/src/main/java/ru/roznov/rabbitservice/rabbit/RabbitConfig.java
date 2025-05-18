package ru.roznov.rabbitservice.rabbit;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.amqp.core.Queue;
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


    @Bean
    public Queue telegramQueue() {
        return new Queue(telegramQueueName, false);
    }

    @Bean
    public Queue notificationsQueue() {
        return new Queue(notificationQueueName, false);
    }
}
