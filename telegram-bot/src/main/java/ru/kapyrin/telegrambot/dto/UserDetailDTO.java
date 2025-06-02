package ru.kapyrin.telegrambot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class UserDetailDTO {
    private Long id;
    private String name;
    private String email;
    private LocalDateTime createdAt;
}
