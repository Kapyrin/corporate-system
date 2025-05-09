package ru.kapyrin.telegrambot.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.kapyrin.telegrambot.exception.NotificationException;

@Component
@Slf4j
public class TelegramNotificationBot extends TelegramLongPollingBot {


    private final String username;

    public TelegramNotificationBot(
            @Value("${telegram.bot.token}") String token,
            @Value("${telegram.bot.username}") String username
    ) {
        super(token);
        this.username = username;
    }
    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String msg = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            if (msg.equals("/start")) {
                log.info("New /start received from telegramId (chatId): {}", chatId);
                sendText(chatId, """
                        Welcome!
                        Your Telegram ID is: %d
                        
                        Please share this ID with your system admin or link it manually using:
                        POST /bot_reg/register?userId=YOUR_ID&telegramId=%d
                        """.formatted(chatId, chatId));


            } else {
                sendText(chatId, "No command found.");
            }
        }
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
