package ru.roznov.rabbitservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.roznov.rabbitservice.controller.NotifyController;
import ru.roznov.rabbitservice.dto.NotifyDetailDTO;
import ru.roznov.rabbitservice.entity.NotificationType;
import ru.roznov.rabbitservice.service.NotificationService;

import java.time.Instant;
import java.util.Collections;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(NotifyController.class)
class NotifyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NotificationService notificationService;
    @Test
    void getNotificationsForUser_ReturnsCorrectData() throws Exception {
        NotifyDetailDTO mockDto = new NotifyDetailDTO(
                1,
                123L,
                NotificationType.WORK_DAY_STARTED,
                "Work started",
                Instant.parse("2023-01-01T09:00:00Z")
        );

        Mockito.when(notificationService.getNotifications(123L))
                .thenReturn(List.of(mockDto));

        mockMvc.perform(get("/api/notifications/123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].userId").value(123))
                .andExpect(jsonPath("$[0].type").value("WORK_DAY_STARTED"))
                .andExpect(jsonPath("$[0].message").value("Work started"));
    }

    @Test
    void getNotificationsForUser_Returns404WhenNoNotifications() throws Exception {
        Long nonExistentUserId = 999L;

        Mockito.when(notificationService.getNotifications(nonExistentUserId))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/notifications/" + nonExistentUserId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Notifications not found for user: " + nonExistentUserId));
    }

}