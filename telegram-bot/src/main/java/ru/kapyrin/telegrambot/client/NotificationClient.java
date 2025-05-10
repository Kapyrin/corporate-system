package ru.kapyrin.telegrambot.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import ru.kapyrin.telegrambot.dto.RecentNotificationDto;
import ru.kapyrin.telegrambot.dto.WorkLogReportDto;

import java.util.List;

@FeignClient(name = "notificationClient", url = "${time.tracking.url}")
public interface NotificationClient {

    @GetMapping("/notifications/{userId}/recent")
    List<RecentNotificationDto> getRecentNotifications(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "3") int limit
    );

    @GetMapping("/api/report/{userId}")
    WorkLogReportDto getReport(
            @PathVariable("userId") Long userId,
            @RequestParam("date") String date
    );
}
