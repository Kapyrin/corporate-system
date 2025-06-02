package ru.kapyrin.telegrambot.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.kapyrin.telegrambot.dto.WorkLogResponseDto;


@FeignClient(name = "workLogClient", url = "${time.tracking.url}")
public interface WorkLogClient {

    @PostMapping("/api/worklog/toggle/{userId}")
    WorkLogResponseDto toggleWorkDay(@PathVariable Long userId);
}
