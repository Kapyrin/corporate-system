package ru.kapyrin.telegrambot.state;

import lombok.Data;

@Data
public class UserRegistrationSession {
    private UserState state;
    private String fullName;
    private String email;
}
