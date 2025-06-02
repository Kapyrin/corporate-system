package ru.kapyrin.telegrambot.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.kapyrin.telegrambot.dto.WorkLogReportDto;
import ru.kapyrin.telegrambot.dto.WorkLogResponseDto;

@FeignClient(name = "timeTrackingClient", url = "${time.tracking.url}")
public interface TimeTrackingClient {


    @GetMapping("/api/report/{userId}")
    WorkLogReportDto getReport(
            @PathVariable("userId") Long userId,
            @RequestParam("date") String date
    );

    @PostMapping("/api/worklog/toggle/{userId}")
    WorkLogResponseDto toggleWorkDay(@PathVariable("userId") Long userId);
}
