package ru.kapyrin.telegrambot.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.kapyrin.telegrambot.client.UserClient;
import ru.kapyrin.telegrambot.dto.UserCreateDTO;
import ru.kapyrin.telegrambot.dto.UserDetailDTO;
import ru.kapyrin.telegrambot.service.UserRegistrationService;

@Service
@RequiredArgsConstructor
public class UserRegistrationServiceImpl implements UserRegistrationService {

    private final UserClient userClient;
    private final TelegramUserService telegramUserService;

    @Override
    public Long register(String name, String email, Long telegramId) {
        UserCreateDTO dto = new UserCreateDTO(name, email);
        UserDetailDTO createdUser = userClient.createUser(dto);
        Long userId = createdUser.getId();

        telegramUserService.register(userId, telegramId);

        return userId;
    }

}
