package ru.kapyrin.telegrambot.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import ru.kapyrin.telegrambot.entity.TelegramUser;

import java.util.List;
import java.util.Optional;

public interface TelegramUserRepository extends JpaRepository<TelegramUser, Long> {
    boolean existsByUserIdAndTelegramId(Long userId, Long telegramId);

    List<TelegramUser> findAllByUserId(Long userId);

}
