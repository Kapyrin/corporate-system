package ru.kapyrin.telegrambot.service;

import ru.kapyrin.telegrambot.dto.NotificationMessage;

public interface NotificationService {
    void sendNotification(NotificationMessage message);
}
