package ru.kapyrin.telegrambot.service.command.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.kapyrin.telegrambot.client.NotificationServiceClient;
import ru.kapyrin.telegrambot.dto.NotifyDetailDTO;
import ru.kapyrin.telegrambot.service.UserRegistryService;
import ru.kapyrin.telegrambot.service.command.BotCommandHandler;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class StatusCommandHandler implements BotCommandHandler {

    private final UserRegistryService registryService;
    private final NotificationServiceClient notificationServiceClient;

    @Override
    public String command() {
        return "/status";
    }

    @Override
    public String handle(Long chatId, String message) {
        List<Long> userIds = registryService.getUserIdsByTelegramId(chatId);
        if (userIds.isEmpty()) {
            return "You are not linked to any users.";
        }

        List<NotifyDetailDTO> result = new ArrayList<>();
        for (Long userId : userIds) {
            try {
                List<NotifyDetailDTO> list = notificationServiceClient.getRecentNotifications(userId, 3);
                result.addAll(list);
            } catch (Exception e) {
                log.warn("Chat {}: Failed to fetch notifications for user {}", chatId, userId, e);
            }
        }

        if (result.isEmpty()) {
            return "No recent notifications found.";
        }

        result.sort(Comparator.comparing(NotifyDetailDTO::getReceivedAt).reversed());
        return format(result);
    }

    private String format(List<NotifyDetailDTO> list) {
        StringBuilder sb = new StringBuilder("Latest notifications:\n\n");
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

        list.stream().limit(3).forEach(n -> sb
                .append(n.getMessage())
                .append(" at ")
                .append(fmt.format(n.getReceivedAt().atZone(java.time.ZoneId.systemDefault())))
                .append("\n\n"));
        return sb.toString().trim();
    }


}
