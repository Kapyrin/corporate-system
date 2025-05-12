package ru.roznov.rabbitservice.rabbit;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import ru.roznov.rabbitservice.dto.NotifyCreateDTO;
import ru.roznov.rabbitservice.service.NotificationService;

@Service
@RequiredArgsConstructor
@Slf4j
public class RabbitReceiver {
    private final NotificationService notificationService;

    @RabbitListener(queues = "notifications.queue")
    public void receive(NotifyCreateDTO dto) {
        log.info("Received message: {}", dto);
        notificationService.saveNotification(dto);
    }
}
