package ru.kapyrin.telegrambot.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Notification message sent to Telegram users")
public class NotificationMessage {

    @Schema(description = "ID of the user who generated the notification", example = "1")
    private Long userId;

    @Schema(description = "Text of the notification", example = "Work day started")
    private String message;

    @Schema(
            description = "Timestamp of the notification (format: dd-MM-yyyy HH:mm)",
            example = "09-05-2025 12:00"
    )

    private Instant timestamp;
}
