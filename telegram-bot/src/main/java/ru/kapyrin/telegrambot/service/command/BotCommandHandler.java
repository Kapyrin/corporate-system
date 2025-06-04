package ru.kapyrin.telegrambot.service.command;

public interface BotCommandHandler {
    String command();

    String handle(Long chatId, String message);
}
