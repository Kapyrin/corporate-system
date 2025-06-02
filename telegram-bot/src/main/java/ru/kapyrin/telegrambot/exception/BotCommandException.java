package ru.kapyrin.telegrambot.exception;

public class BotCommandException extends RuntimeException {

    public BotCommandException(String message) {
        super(message);
    }

    public BotCommandException(String message, Throwable cause) {
        super(message, cause);
    }
}
