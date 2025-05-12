package com.example.timetrackingservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationMessageDto {
    private Long userId;
    private NotificationEventType type;
    private String timestamp;
}
