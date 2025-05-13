package ru.roznov.rabbitservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.Instant;

@Data
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
