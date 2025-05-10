package ru.kapyrin.telegrambot.service;

import java.util.List;

public interface UserRegistryService {
    void register(Long userId, Long channelId);
    List<Long> getChannelIdsByUserId(Long userId);
    List<Long> getUserIdsByTelegramId(Long telegramId);
}
