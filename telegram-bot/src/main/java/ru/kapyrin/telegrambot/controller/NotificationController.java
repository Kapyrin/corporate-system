package ru.kapyrin.telegrambot.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.kapyrin.telegrambot.dto.NotificationMessage;
import ru.kapyrin.telegrambot.service.NotificationService;

@RestController
@RequestMapping("/notify")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping
    @Operation(
            summary = "Send a notification to Telegram users",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Notification with userId and timestamp",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = NotificationMessage.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "userId": 1,
                                      "message": "Work day started",
                                      "timestamp": "2025-05-09 12:00"
                                    }
                                    """)
                    )
            )
    )
    @ApiResponse(responseCode = "200", description = "Notification sent")
    @ApiResponse(responseCode = "500", description = "Failed to send notification")
    public ResponseEntity<String> notify(
            @RequestBody NotificationMessage message
    ) {
        notificationService.sendNotification(message);
        return ResponseEntity.ok("Notification sent.");
    }
}
