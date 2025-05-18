package ru.roznov.rabbitservice.rabbit;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.roznov.rabbitservice.entity.Notification;

@Service
@RequiredArgsConstructor
public class MessageSender {
    @Value("${queue.name.telegram}")
    private String telegramQueueName;

    @Value("${queue.name.notify}")
    private String notificationQueueName;
    private final RabbitTemplate amqpTemplate;
    private final ObjectMapper objectMapper;

    public void sendToTelegram(Notification message) {
        amqpTemplate.convertAndSend(telegramQueueName, message);
    }

    public void sendToNotification(String txt) {
        amqpTemplate.convertAndSend("notifications-queue", txt);
    }
}
