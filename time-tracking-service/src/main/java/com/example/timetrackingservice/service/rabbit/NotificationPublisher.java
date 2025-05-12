package com.example.timetrackingservice.service.rabbit;

import com.example.timetrackingservice.config.RabbitConfig;
import com.example.timetrackingservice.dto.NotificationMessageDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationPublisher {

    private final RabbitTemplate rabbitTemplate;

    public void publish(NotificationMessageDto message) {
        log.info("Sending message to exchange='{}' with routingKey='{}': {}",
                RabbitConfig.EXCHANGE, RabbitConfig.ROUTING_KEY, message);

        rabbitTemplate.convertAndSend(
                RabbitConfig.EXCHANGE,
                RabbitConfig.ROUTING_KEY,
                message
        );
    }
}
