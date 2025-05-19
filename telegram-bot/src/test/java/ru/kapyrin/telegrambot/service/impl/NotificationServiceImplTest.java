package ru.kapyrin.telegrambot.service.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import ru.kapyrin.telegrambot.dto.NotificationMessage;
import ru.kapyrin.telegrambot.entity.TelegramUser;
import ru.kapyrin.telegrambot.repository.TelegramUserRepository;
import ru.kapyrin.telegrambot.service.NotificationService;
import ru.kapyrin.telegrambot.service.TelegramNotificationBot;

import java.time.Instant;
import java.util.List;

import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
class NotificationServiceImplTest {

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private TelegramUserRepository repository;

    @MockBean
    private TelegramNotificationBot bot;

    @Test
    @DisplayName("Should send notification to all user telegram channels")
    void testSendNotificationToMultipleChannels() {
        Long userId = 42L;
        repository.save(TelegramUser.builder().userId(userId).telegramId(101L).build());
        repository.save(TelegramUser.builder().userId(userId).telegramId(102L).build());

        NotificationMessage message = new NotificationMessage(userId, "Hello from test", Instant.now());

        notificationService.sendNotification(message);

        verify(bot).sendText(eq(101L), contains("Hello from test"));
        verify(bot).sendText(eq(102L), contains("Hello from test"));
    }

    @Test
    @DisplayName("Should not send message if no channels found")
    void testSendNotificationToEmptyChannelList() {
        Long userId = 100L;
        NotificationMessage message = new NotificationMessage(userId, "Empty", Instant.now());

        notificationService.sendNotification(message);

        verify(bot, never()).sendText(anyLong(), anyString());
    }
}
