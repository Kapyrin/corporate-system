package ru.kapyrin.telegrambot.service;

public interface UserRegistrationService {
    Long register(String name, String email, Long telegramId);

}
