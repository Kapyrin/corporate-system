package ru.kapyrin.telegrambot.exception;

public class NotificationSendException extends RuntimeException {
    public NotificationSendException(Long chatId, Throwable cause) {
        super("Failed to send message to Telegram chatId: " + chatId, cause);
    }
}
