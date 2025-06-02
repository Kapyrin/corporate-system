package ru.kapyrin.telegrambot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.kapyrin.telegrambot.entity.NotificationType;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotifyDetailDTO {
    private int id;
    private Long userId;
    private NotificationType type;
    private String message;
    private Instant receivedAt;
}