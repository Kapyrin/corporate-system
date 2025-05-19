package ru.kapyrin.telegrambot.service.rabbit;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import ru.kapyrin.telegrambot.dto.NotificationMessage;
import ru.kapyrin.telegrambot.service.NotificationService;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class TelegramRabbitReceiverTest {

    @Test
    @DisplayName("Should delegate received message to NotificationService")
    void testReceiveMessage() {
        NotificationService mockService = mock(NotificationService.class);
        TelegramRabbitReceiver receiver = new TelegramRabbitReceiver(mockService);
        NotificationMessage message = new NotificationMessage(5L, "Test message", Instant.now());


        receiver.receive(message);

        ArgumentCaptor<NotificationMessage> captor = ArgumentCaptor.forClass(NotificationMessage.class);
        verify(mockService, times(1)).sendNotification(captor.capture());

        assertThat(captor.getValue()).isEqualTo(message);
    }
}
