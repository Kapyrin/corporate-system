package ru.roznov.rabbitservice.rabbit;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class RabbitReceiver {
    @RabbitListener(queues = {"notifications-queue"})
    public void receive(String message) {
    }
}
