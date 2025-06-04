package ru.kapyrin.telegrambot.service.command.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.kapyrin.telegrambot.client.TimeTrackingClient;
import ru.kapyrin.telegrambot.dto.WorkLogResponseDto;
import ru.kapyrin.telegrambot.exception.BotCommandException;
import ru.kapyrin.telegrambot.service.UserRegistryService;
import ru.kapyrin.telegrambot.service.command.BotCommandHandler;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class WorkCommandHandler implements BotCommandHandler {

    private final UserRegistryService registryService;
    private final TimeTrackingClient notificationClient;

    @Override
    public String command() {
        return "/work";
    }

    @Override
    public String handle(Long chatId, String message) {
        List<Long> userIds = registryService.getUserIdsByTelegramId(chatId);
        if (userIds.size() != 1) {
            throw new BotCommandException("User should be linked to exactly one userId, but found: " + userIds.size());
        }

        Long userId = userIds.get(0);

        try {
            WorkLogResponseDto dto = notificationClient.toggleWorkDay(userId);
            log.info("Chat {}: toggled work session for user {}, result: {}", chatId, userId, dto);
            return formatResponse(dto);
        } catch (Exception e) {
            log.error("Chat {}: error toggling work session for user {}", chatId, userId, e);
            throw new BotCommandException("Failed to toggle work session", e);
        }
    }

    private String formatResponse(WorkLogResponseDto dto) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        return dto.getEndTime() == null
                ? "Work session started at " + formatter.format(dto.getStartTime())
                : "Work session ended at " + formatter.format(dto.getEndTime());
    }
}
