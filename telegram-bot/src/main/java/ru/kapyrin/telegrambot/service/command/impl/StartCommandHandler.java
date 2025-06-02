package ru.kapyrin.telegrambot.service.command.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.kapyrin.telegrambot.service.UserRegistryService;
import ru.kapyrin.telegrambot.service.command.BotCommandHandler;
import ru.kapyrin.telegrambot.state.UserRegistrationSession;
import ru.kapyrin.telegrambot.state.UserSessionContext;
import ru.kapyrin.telegrambot.state.UserState;

@Component
@RequiredArgsConstructor
@Slf4j
public class StartCommandHandler implements BotCommandHandler {

    private final UserRegistryService registryService;
    private final UserSessionContext sessionContext;

    @Override
    public boolean supports(String command, Long chatId) {
        return "/start".equalsIgnoreCase(command);
    }

    @Override
    public String handle(Long chatId, String message) {
        if (registryService.existsByTelegramId(chatId)) {
            log.info("Chat {}: user already registered", chatId);
            return "You are already registered.";
        }

        UserRegistrationSession session = new UserRegistrationSession();
        session.setState(UserState.AWAITING_FULL_NAME);
        sessionContext.setSession(chatId, session);
        log.info("Chat {}: new registration started", chatId);
        return "Welcome! Please enter your full name:";
    }
}
