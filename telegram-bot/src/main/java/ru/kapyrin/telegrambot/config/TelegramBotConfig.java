package ru.kapyrin.telegrambot.config;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import ru.kapyrin.telegrambot.service.TelegramNotificationBot;

@Configuration
@RequiredArgsConstructor
public class TelegramBotConfig {

    private final TelegramNotificationBot bot;

    @PostConstruct
    public void registerBot() throws Exception {
        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
        botsApi.registerBot(bot);
    }
}
