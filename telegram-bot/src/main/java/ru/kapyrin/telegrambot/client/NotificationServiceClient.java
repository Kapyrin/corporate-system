package ru.kapyrin.telegrambot.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import ru.kapyrin.telegrambot.dto.NotifyDetailDTO;

import java.util.List;

@FeignClient(name = "notificationServiceClient", url = "${notification.service.url}")
public interface  NotificationServiceClient {
    @GetMapping("/api/notifications/{userId}/recent")
    List<NotifyDetailDTO> getRecentNotifications(
            @PathVariable("userId") Long userId,
            @RequestParam(name = "limit", defaultValue = "3") int limit
    );
}
