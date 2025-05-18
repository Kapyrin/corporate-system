package ru.roznov.rabbitservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.roznov.rabbitservice.dto.NotifyCreateDTO;
import ru.roznov.rabbitservice.dto.NotifyDetailDTO;
import ru.roznov.rabbitservice.rabbit.MessageSender;
import ru.roznov.rabbitservice.service.NotificationService;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotifyController {
    private final NotificationService notificationService;
    private final MessageSender messageSender;

    @Operation(summary = "Get notify for user by id", description = "Returns all notify for user by id")
    @ApiResponse(responseCode = "200", description = "Notifications returned")
    @ApiResponse(responseCode = "404", description = "No notifications for this user")
    @GetMapping("/{userId}")
    public ResponseEntity<List<NotifyDetailDTO>> getNotificationsForUser(@Parameter(name = "userId", description = "User ID") @PathVariable Long userId) {
        return ResponseEntity.ok(notificationService.getNotifications(userId));
    }

    @Operation(summary = "Get recent notifications for user", description = "Returns a list of recent notifications for the specified user ID, sorted by timestamp")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved recent notifications")
    @ApiResponse(responseCode = "400", description = "Invalid limit parameter")
    @GetMapping("/{userId}/recent")
    public ResponseEntity<List<NotifyDetailDTO>> getRecentNotifications(@PathVariable Long userId, @RequestParam(defaultValue = "10") @Parameter(name = "limit", description = "Number of recent notifications to return (default: 10)", example = "5") int limit) {
        if (limit <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Limit must be positive");
        }

        return ResponseEntity.ok(notificationService.getRecentNotifications(userId, limit));

    }
    @Operation(description = "Send message to RabbitMQ in queue with delay")
    @ApiResponse(responseCode = "204")
    @PostMapping("/test")
    public void testNotification(@RequestBody String txt) throws JsonProcessingException {
        messageSender.sendToNotification(txt);
    }
}
