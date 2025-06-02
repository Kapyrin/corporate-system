package ru.kapyrin.telegrambot.state;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class UserSessionContext {

    private final Map<Long, UserRegistrationSession> sessionMap = new ConcurrentHashMap<>();

    public void setSession(Long chatId, UserRegistrationSession session) {
        sessionMap.put(chatId, session);
    }

    public UserRegistrationSession getSession(Long chatId) {
        return sessionMap.get(chatId);
    }

    public boolean hasSession(Long chatId) {
        return sessionMap.containsKey(chatId);
    }

    public void clear(Long chatId) {
        sessionMap.remove(chatId);
    }
}
