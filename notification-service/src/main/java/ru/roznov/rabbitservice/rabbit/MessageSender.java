package ru.roznov.rabbitservice.rabbit;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.roznov.rabbitservice.dto.NotifyCreateDTO;

@Service
@RequiredArgsConstructor
@Slf4j
public class MessageSender {
    @Value("${rabbit.exchange}")
    private String exchange;

    @Value("${queue.name.telegram.routing-key}")
    private String telegramRoutingKey;

    private final RabbitTemplate rabbitTemplate;

    public void sendToTelegram(NotifyCreateDTO dto) {
        log.info("Sending to telegram queue: {}", dto);
        rabbitTemplate.convertAndSend(exchange, telegramRoutingKey, dto);
    }

//    public void sendToNotification(NotifyCreateDTO dto) {
//        rabbitTemplate.convertAndSend(notificationQueueName, dto);
//    }
}
