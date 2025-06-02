package ru.kapyrin.telegrambot.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.kapyrin.telegrambot.dto.NotificationMessage;
import ru.kapyrin.telegrambot.service.NotificationService;
import ru.kapyrin.telegrambot.service.TelegramNotificationBot;
import ru.kapyrin.telegrambot.service.UserRegistryService;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationServiceImpl implements NotificationService {

    private final UserRegistryService registryService;
    private final TelegramNotificationBot bot;

    @Override
    public void sendNotification(NotificationMessage message) {
        List<Long> chatIds = registryService.getChannelIdsByUserId(message.getUserId());

        if (chatIds.isEmpty()) {
            log.warn("No chatIds found for userId {}", message.getUserId());
            return;
        }

        String text = formatMessage(message);

        for (Long chatId : chatIds) {
            try {
                bot.sendText(chatId, text);
            } catch (Exception e) {
                log.error("Failed to send notification to chatId {}: {}", chatId, e.getMessage(), e);

            }
        }
    }

    private String formatMessage(NotificationMessage msg) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        return "Notification\n\n" +
                "User ID: " + msg.getUserId() + "\n" +
                "Time: " + formatter.format(msg.getTimestamp()) + "\n" +
                "Message:\n" + msg.getMessage();
    }
}
