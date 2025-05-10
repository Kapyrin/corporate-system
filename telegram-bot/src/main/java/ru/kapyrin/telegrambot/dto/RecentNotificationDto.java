package ru.kapyrin.telegrambot.dto;



import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RecentNotificationDto {
    private String message;
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm")
    private LocalDateTime receivedAt;
}
