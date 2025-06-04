package ru.kapyrin.telegrambot.service.command.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.kapyrin.telegrambot.service.UserRegistrationService;
import ru.kapyrin.telegrambot.state.UserRegistrationSession;
import ru.kapyrin.telegrambot.state.UserSessionContext;
import ru.kapyrin.telegrambot.state.UserState;

@Component
@RequiredArgsConstructor
@Slf4j
public class SessionCommandHandler {

    private final UserSessionContext sessionContext;
    private final UserRegistrationService registrationService;

    public boolean canHandle(Long chatId) {
        return sessionContext.hasSession(chatId);
    }

    public String handle(Long chatId, String message) {
        UserRegistrationSession session = sessionContext.getSession(chatId);

        switch (session.getState()) {
            case AWAITING_FULL_NAME -> {
                log.info("Chat {}: received full name '{}'", chatId, message);
                session.setFullName(message);
                session.setState(UserState.AWAITING_EMAIL);
                return "Please enter your email address (you may leave it blank):";
            }

            case AWAITING_EMAIL -> {
                String email = message.isBlank() ? null : message;
                session.setEmail(email);
                log.info("Chat {}: received email '{}', attempting registration", chatId, email);

                try {
                    Long userId = registrationService.register(
                            session.getFullName(), session.getEmail(), chatId
                    );
                    log.info("Chat {}: registration successful, userId={}", chatId, userId);
                    return "Registration completed successfully. Your ID is: " + userId;
                } catch (Exception e) {
                    log.error("Chat {}: registration failed: {}", chatId, e.getMessage(), e);
                    return "An error occurred during registration: " + e.getMessage();
                } finally {
                    sessionContext.clear(chatId);
                }
            }

            default -> {
                log.warn("Chat {}: unexpected user state '{}'", chatId, session.getState());
                sessionContext.clear(chatId);
                return "Unexpected state. Restart registration with /start.";
            }
        }
    }
}
