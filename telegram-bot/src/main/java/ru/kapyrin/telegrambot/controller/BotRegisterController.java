package ru.kapyrin.telegrambot.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.kapyrin.telegrambot.service.UserRegistryService;

@RestController
@RequestMapping("/bot_reg")
@RequiredArgsConstructor
public class BotRegisterController {

    private final UserRegistryService registryService;
    @Operation(
            summary = "Register Telegram ID for a user",
            description = "Creates a link between a user in the system and a Telegram user ID. Required to receive notifications."
    )
    @ApiResponse(responseCode = "200", description = "Registration successful")
    @ApiResponse(responseCode = "409", description = "This user is already registered with the provided Telegram ID")
    @PostMapping("/register")
    public ResponseEntity<String> registerTelegramId(
            @RequestParam Long userId,
            @RequestParam Long telegramId
    ) {
        registryService.register(userId, telegramId);
        return ResponseEntity.ok("the link is registered: userId = " + userId + ", telegramId = " + telegramId);
    }
}
