package ru.kapyrin.telegrambot.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.kapyrin.telegrambot.service.TelegramNotificationBot;

import java.util.List;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class TelegramCommandConfig {

    private final TelegramNotificationBot bot;

    @Bean
    public Boolean registerTelegramCommands() {
        List<BotCommand> commands = List.of(
                new BotCommand("/start", "Register or check your status"),
                new BotCommand("/status", "Show latest 3 notifications"),
                new BotCommand("/report", "Get today’s work report"),
                new BotCommand("/work", "Start or finish workday")
        );

        try {
            bot.execute(SetMyCommands.builder().commands(commands).build());
            log.info("Telegram bot commands registered successfully");
        } catch (TelegramApiException e) {
            log.error("Failed to register bot commands", e);
        }

        return true;
    }
}
