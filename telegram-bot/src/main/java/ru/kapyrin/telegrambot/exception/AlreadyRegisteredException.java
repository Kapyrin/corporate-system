package ru.kapyrin.telegrambot.exception;

public class AlreadyRegisteredException extends RuntimeException {
public AlreadyRegisteredException(Long userId, Long telegramId) {
    super("User with id " + userId + " already registered in telegram chat with id " + telegramId);
}
}
