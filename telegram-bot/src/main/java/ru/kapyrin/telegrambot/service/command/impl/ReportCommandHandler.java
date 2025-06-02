package ru.kapyrin.telegrambot.service.command.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.kapyrin.telegrambot.client.TimeTrackingClient;
import ru.kapyrin.telegrambot.dto.WorkLogReportDto;
import ru.kapyrin.telegrambot.service.UserRegistryService;
import ru.kapyrin.telegrambot.service.command.BotCommandHandler;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class ReportCommandHandler implements BotCommandHandler {

    private final UserRegistryService registryService;
    private final TimeTrackingClient notificationClient;

    @Override
    public boolean supports(String command, Long chatId) {
        return "/report".equalsIgnoreCase(command);
    }

    @Override
    public String handle(Long chatId, String message) {
        List<Long> userIds = registryService.getUserIdsByTelegramId(chatId);
        if (userIds.isEmpty()) {
            return "You are not linked to any users.";
        }

        String today = LocalDate.now().toString();
        StringBuilder sb = new StringBuilder("Work report for today:\n\n");

        for (Long userId : userIds) {
            try {
                WorkLogReportDto dto = notificationClient.getReport(userId, today);
                if (dto.getDaysWorked() == 0) {
                    sb.append("User ID ").append(userId).append(": No work logs today.\n\n");
                    continue;
                }
                sb.append("User ID: ").append(userId).append("\n")
                        .append("Worked: ").append(dto.getTotalWorked()).append("\n")
                        .append("Overtime: ").append(dto.getOvertimeHours()).append(" hours\n\n");
            } catch (Exception e) {
                log.warn("Chat {}: Error fetching report for user {}: {}", chatId, userId, e.getMessage());
                sb.append("Failed to load report for user ").append(userId).append("\n\n");
            }
        }

        return sb.toString().trim();
    }
}
