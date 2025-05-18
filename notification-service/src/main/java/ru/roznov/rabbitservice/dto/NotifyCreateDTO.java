package ru.roznov.rabbitservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.roznov.rabbitservice.entity.NotificationType;


import java.io.Serializable;
import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotifyCreateDTO {

    @NotNull
    private Long userId;

    @NotNull(message = "Type is required")
    private String type;

    @NotBlank(message = "Message is required")
    @Size(max = 255, message = "Message must be at most 255 characters")
    private String message;
    @NotNull
    private Instant receivedAt;
}