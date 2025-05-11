package ru.roznov.rabbitservice.rabbit;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessageSender {
    @Value("${queue.name.telegram}")
    private String telegramQueueName;

    @Value("${queue.name.notify}")
    private String notificationQueueName;
    private final RabbitTemplate rabbitTemplate;

    public void sendToTelegram(String message) {
        rabbitTemplate.convertAndSend(telegramQueueName,message);
    }
    public void sendToNotification(String message) {
        rabbitTemplate.convertAndSend(notificationQueueName, message);
    }
}
