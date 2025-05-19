package ru.kapyrin.telegrambot.service.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import ru.kapyrin.telegrambot.entity.TelegramUser;
import ru.kapyrin.telegrambot.exception.AlreadyRegisteredException;
import ru.kapyrin.telegrambot.repository.TelegramUserRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
@Import(TelegramUserService.class)
class TelegramUserServiceTest {

    @Autowired
    private TelegramUserService service;

    @Autowired
    private TelegramUserRepository repository;

    @Test
    @DisplayName("Should register a user and save correct data")
    void testRegisterSuccess() {
        Long userId = 1L;
        Long telegramId = 111L;

        service.register(userId, telegramId);

        List<TelegramUser> users = repository.findAll();
        assertThat(users).hasSize(1);

        TelegramUser savedUser = users.get(0);
        assertThat(savedUser.getUserId()).isEqualTo(userId);
        assertThat(savedUser.getTelegramId()).isEqualTo(telegramId);
    }


    @Test
    @DisplayName("Throws exception on duplicate registration")
    void testRegisterAlreadyExists() {
        service.register(2L, 222L);
        assertThrows(AlreadyRegisteredException.class, () -> service.register(2L, 222L));
    }

    @Test
    @DisplayName("Find Telegram IDs by userId")
    void testGetChannelIdsByUserId() {
        service.register(3L, 333L);
        service.register(3L, 334L);

        List<Long> ids = service.getChannelIdsByUserId(3L);
        assertThat(ids).containsExactlyInAnyOrder(333L, 334L);
    }

    @Test
    @DisplayName("Find userIds by Telegram ID")
    void testGetUserIdsByTelegramId() {
        service.register(4L, 444L);
        service.register(5L, 444L);

        List<Long> ids = service.getUserIdsByTelegramId(444L);
        assertThat(ids).containsExactlyInAnyOrder(4L, 5L);
    }
}
