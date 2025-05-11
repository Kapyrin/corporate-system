package ru.roznov.rabbitservice.config;

import lombok.Setter;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Setter
public class RabbitConfig {
    @Value("${queue.name.telegram}")
    private String telegramQueueName;

    @Value("${queue.name.notify}")
    private String notificationQueueName;

    @Bean
    public Queue telegramQueue() {
        return new Queue(telegramQueueName, true);
    }

    @Bean
    public Queue notificationsQueue() {
        return new Queue(notificationQueueName, true);
    }
}
