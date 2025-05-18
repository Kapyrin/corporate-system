package ru.roznov.rabbitservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.roznov.rabbitservice.dto.NotifyDetailDTO;
import ru.roznov.rabbitservice.exception.InvalidRequestParameterException;
import ru.roznov.rabbitservice.exception.NotificationNotFoundException;
import ru.roznov.rabbitservice.service.NotificationService;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotifyController {
    private final NotificationService notificationService;

    @Operation(summary = "Get notify for user by id", description = "Returns all notify for user by id")
    @ApiResponse(responseCode = "200", description = "Notifications returned")
    @ApiResponse(responseCode = "404", description = "No notifications for this user")
    @GetMapping("/{userId}")
    public ResponseEntity<List<NotifyDetailDTO>> getNotificationsForUser(@PathVariable Long userId) {
        List<NotifyDetailDTO> result = notificationService.getNotifications(userId);
        if (result.isEmpty()) {
            throw new NotificationNotFoundException(userId);
        }
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Get recent notifications for user", description = "Returns a list of recent notifications for the specified user ID, sorted by timestamp")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved recent notifications")
    @ApiResponse(responseCode = "400", description = "Invalid limit parameter")
    @GetMapping("/{userId}/recent")
    public ResponseEntity<List<NotifyDetailDTO>> getRecentNotifications(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "10") int limit
    ) {
        if (limit <= 0) {
            throw new InvalidRequestParameterException("limit", "Must be between 1 and 100");
        }
        return ResponseEntity.ok(notificationService.getRecentNotifications(userId, limit));
    }
}
