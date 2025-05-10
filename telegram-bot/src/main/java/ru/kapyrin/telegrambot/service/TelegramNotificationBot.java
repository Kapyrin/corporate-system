package ru.kapyrin.telegrambot.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.kapyrin.telegrambot.client.NotificationClient;
import ru.kapyrin.telegrambot.config.TelegramBotProperties;
import ru.kapyrin.telegrambot.dto.RecentNotificationDto;
import ru.kapyrin.telegrambot.dto.WorkLogReportDto;
import ru.kapyrin.telegrambot.exception.NotificationException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class TelegramNotificationBot extends TelegramLongPollingBot {

    private final String username;
    private final UserRegistryService registryService;
    private final NotificationClient notificationClient;

    public TelegramNotificationBot(
            TelegramBotProperties config,
            UserRegistryService registryService,
            NotificationClient notificationClient
    ) {
        super(config.getToken());
        this.username = config.getUsername();
        this.registryService = registryService;
        this.notificationClient = notificationClient;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String msg = update.getMessage().getText().trim();
            long chatId = update.getMessage().getChatId();

            switch (msg) {
                case "/start" -> handleStart(chatId);
                case "/status" -> handleStatus(chatId);
                case "/report" -> handleReport(chatId);
                default -> sendText(chatId, " Unknown command: " + msg);
            }
        }
    }

    private void handleStart(Long chatId) {
        log.info("New /start received from telegramId (chatId): {}", chatId);
        sendText(chatId, """
                Welcome!
                Your Telegram ID is: %d
                
                Please share this ID with your system admin or link it manually using:
                POST /bot_reg/register?userId=YOUR_ID&telegramId=%d
                """.formatted(chatId, chatId));
    }

    private void handleStatus(Long chatId) {
        List<Long> userIds = registryService.getUserIdsByTelegramId(chatId);

        if (userIds.isEmpty()) {
            sendText(chatId, "You are not linked to any users.");
            return;
        }

        List<RecentNotificationDto> result = new ArrayList<>();

        for (Long userId : userIds) {
            try {
                List<RecentNotificationDto> list = notificationClient.getRecentNotifications(userId, 3);
                result.addAll(list);
            } catch (Exception e) {
                log.warn("Failed to load notifications for user {}", userId);
            }
        }

        if (result.isEmpty()) {
            sendText(chatId, "No recent notifications found.");
            return;
        }

        result.sort((a, b) -> b.getReceivedAt().compareTo(a.getReceivedAt()));

        sendText(chatId, fromNotificationsToMessage(result));
    }

    private String fromNotificationsToMessage(List<RecentNotificationDto> notifications) {
        StringBuilder sb = new StringBuilder("📨 Latest notifications:\n\n");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

        for (int i = 0; i < Math.min(3, notifications.size()); i++) {
            RecentNotificationDto dto = notifications.get(i);
            sb.append(dto.getMessage())
                    .append(" at ")
                    .append(formatter.format(dto.getReceivedAt()))
                    .append("\n\n");
        }

        return sb.toString().trim();
    }


    private void handleReport(Long chatId) {
        List<Long> userIds = registryService.getUserIdsByTelegramId(chatId);

        if (userIds.isEmpty()) {
            sendText(chatId, "You are not linked to any users.");
            return;
        }

        String today = LocalDate.now().toString();
        StringBuilder reportMessage = new StringBuilder("Work report for today:\n\n");

        for (Long userId : userIds) {
            try {
                WorkLogReportDto report = notificationClient.getReport(userId, today);

                if (report.getDaysWorked() == 0) {
                    reportMessage.append("User ID: ").append(userId).append("\n")
                            .append(" No work logs found for today.\n\n");
                    continue;
                }

                reportMessage.append(" User ID: ").append(userId).append("\n")
                        .append("Worked: ").append(report.getTotalWorked()).append("\n")
                        .append("Overtime: ").append(report.getOvertimeHours()).append(" hours\n\n");

            } catch (Exception e) {
                log.warn("Failed to load report for user {}", userId, e);
                reportMessage.append("Failed to load report for user ").append(userId).append("\n\n");
            }
        }

        sendText(chatId, reportMessage.toString().trim());
    }


    @Override
    public String getBotUsername() {
        return username;
    }

    public void sendText(Long chatId, String text) {
        try {
            execute(SendMessage.builder()
                    .chatId(chatId.toString())
                    .text(text)
                    .build());
        } catch (Exception e) {
            throw new NotificationException("Telegram error for chatId " + chatId, e);
        }
    }

}
