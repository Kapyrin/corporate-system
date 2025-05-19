package ru.kapyrin.telegrambot.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.kapyrin.telegrambot.exception.AlreadyRegisteredException;
import ru.kapyrin.telegrambot.exception.GlobalExceptionHandler;
import ru.kapyrin.telegrambot.service.UserRegistryService;

import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BotRegisterController.class)
@Import(GlobalExceptionHandler.class)
public class BotRegisterControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRegistryService registryService;

    @Test
    @DisplayName("POST /bot_reg/register - success")
    void testRegisterTelegramId_success() throws Exception {
        mockMvc.perform(post("/bot_reg/register")
                        .param("userId", "1")
                        .param("telegramId", "1001")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(content().string("the link is registered: userId = 1, telegramId = 1001"));
    }

    @Test
    @DisplayName("POST /bot_reg/register - already registered")
    void testRegisterTelegramId_conflict() throws Exception {
        doThrow(new AlreadyRegisteredException(1L, 1001L))
                .when(registryService).register(1L, 1001L);

        mockMvc.perform(post("/bot_reg/register")
                        .param("userId", "1")
                        .param("telegramId", "1001")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isConflict())
                .andExpect(content().string("User with id 1 already registered in telegram chat with id 1001"));
    }
}
