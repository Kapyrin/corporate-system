package ru.kapyrin.telegrambot.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.kapyrin.telegrambot.dto.NotificationMessage;
import ru.kapyrin.telegrambot.service.NotificationService;
import ru.kapyrin.telegrambot.service.TelegramNotificationBot;
import ru.kapyrin.telegrambot.service.UserRegistryService;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final UserRegistryService registryService;
    private final TelegramNotificationBot bot;

    @Override
    public void sendNotification(NotificationMessage message) {
        registryService.getChannelIdsByUserId(message.getUserId())
                .forEach(chatId -> bot.sendText(chatId, formatMessage(message)));

    }

    private String formatMessage(NotificationMessage msg) {
        return "Notification:\n" + msg.getMessage() + "\n " + msg.getTimestamp();
    }
}
