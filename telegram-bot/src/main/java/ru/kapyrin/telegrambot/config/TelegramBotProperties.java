package ru.kapyrin.telegrambot.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Data
@ConfigurationProperties(prefix = "telegram.bot")
public class TelegramBotProperties {
    String token;
    String username;
}
