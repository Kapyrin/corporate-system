package ru.kapyrin.telegrambot.service.command;

public interface BotCommandHandler {
    boolean supports(String command, Long chatId);
    String handle(Long chatId, String message);
}
