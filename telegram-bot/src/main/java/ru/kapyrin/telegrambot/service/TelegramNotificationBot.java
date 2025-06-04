package ru.kapyrin.telegrambot.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.kapyrin.telegrambot.config.TelegramBotProperties;
import ru.kapyrin.telegrambot.service.command.BotCommandHandler;

import ru.kapyrin.telegrambot.service.command.impl.SessionCommandHandler;
import ru.kapyrin.telegrambot.state.UserSessionContext;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class TelegramNotificationBot extends TelegramLongPollingBot {

    private final TelegramBotProperties config;
    private final List<BotCommandHandler> commandHandlers;
    private final SessionCommandHandler sessionCommandHandler;
    private final UserSessionContext sessionContext;

    @Override
    public void onUpdateReceived(Update update) {
        if (!update.hasMessage() || !update.getMessage().hasText()) {
            return;
        }

        String message = update.getMessage().getText().trim();
        Long chatId = update.getMessage().getChatId();

        try {

            if (sessionContext.hasSession(chatId)) {
                String response = sessionCommandHandler.handle(chatId, message);
                sendText(chatId, response);
                return;
            }

            for (BotCommandHandler handler : commandHandlers) {
                if (handler.command().equalsIgnoreCase(message)) {
                    String response = handler.handle(chatId, message);
                    sendText(chatId, response);
                    return;
                }
            }

            sendText(chatId, "Unknown command: " + message);

        } catch (Exception e) {
            log.error("Error processing command '{}': {}", message, e.getMessage(), e);
            sendText(chatId, "Error: " + e.getMessage());
        }
    }

    public void sendText(Long chatId, String text) {
        try {
            execute(SendMessage.builder()
                    .chatId(chatId.toString())
                    .text(text)
                    .build());
        } catch (Exception e) {
            log.error("Failed to send message to chat {}", chatId, e);
            throw new RuntimeException("Telegram error", e);
        }
    }

    @Override
    public String getBotUsername() {
        return config.getUsername();
    }

    @Override
    public String getBotToken() {
        return config.getToken();
    }
}
