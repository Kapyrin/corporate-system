package ru.kapyrin.telegrambot.service.rabbit;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import ru.kapyrin.telegrambot.dto.NotificationMessage;
import ru.kapyrin.telegrambot.service.NotificationService;

@Component
@RequiredArgsConstructor
@Slf4j
public class TelegramRabbitReceiver {

    private final NotificationService notificationService;

    @RabbitListener(queues = "${queue.name.telegram}")
    public void receive(NotificationMessage message) {
        log.info(" Received from queue: {}", message);
        notificationService.sendNotification(message);
    }
}