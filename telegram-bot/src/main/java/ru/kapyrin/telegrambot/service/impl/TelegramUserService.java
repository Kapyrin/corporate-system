package ru.kapyrin.telegrambot.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.kapyrin.telegrambot.entity.TelegramUser;
import ru.kapyrin.telegrambot.exception.AlreadyRegisteredException;
import ru.kapyrin.telegrambot.repository.TelegramUserRepository;
import ru.kapyrin.telegrambot.service.UserRegistryService;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TelegramUserService implements UserRegistryService {
    private final TelegramUserRepository repo;

    @Override
    public void register(Long userId, Long telegramId) {
        boolean exists = repo.existsByUserIdAndTelegramId(userId, telegramId);

        if (exists) {
            throw new AlreadyRegisteredException(userId, telegramId);
        }

        TelegramUser user = TelegramUser.builder()
                .userId(userId)
                .telegramId(telegramId)
                .build();
        repo.save(user);
    }


    @Override
    public List<Long> getChannelIdsByUserId(Long userId) {
        return repo.findAllByUserId(userId)
                .stream()
                .map(TelegramUser::getTelegramId)
                .toList();
    }

    @Override
    public List<Long> getUserIdsByTelegramId(Long telegramId) {
        return repo.findAllByTelegramId(telegramId).stream()
                .map(TelegramUser::getUserId)
                .distinct()
                .toList();
    }
}
