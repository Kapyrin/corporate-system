package ru.kapyrin.telegrambot.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import ru.kapyrin.telegrambot.entity.TelegramUser;

import java.util.List;

public interface TelegramUserRepository extends JpaRepository<TelegramUser, Long> {
    boolean existsByUserIdAndTelegramId(Long userId, Long telegramId);

    List<TelegramUser> findAllByUserId(Long userId);

    List<TelegramUser> findAllByTelegramId(Long telegramId);

}
