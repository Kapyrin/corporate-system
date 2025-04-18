package com.example.timetrackingservice.vladimir;

import com.example.timetrackingservice.client.UserClient;
import com.example.timetrackingservice.dto.WorkLogResponseDto;
import com.example.timetrackingservice.service.WorkLogCoreService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;


public class WorkLogServiceTest extends AbstractIntegrationTest {
    @Autowired
    private WorkLogCoreService workLogService;
    @MockBean
    private UserClient userClient;


    @Test
    void testStartWorkDay() {
        when(userClient.userExists(anyLong())).thenReturn(true);

        WorkLogResponseDto responseDto = workLogService.switchWorkDay(1L);

        assertNotNull(responseDto);
        assertEquals(1L, responseDto.getUserId());
        assertNotNull(responseDto.getStartTime());
    }

    @Test
    void testStopWorkDay() {
        when(userClient.userExists(anyLong())).thenReturn(true);
        workLogService.switchWorkDay(1L);
        WorkLogResponseDto responseDto = workLogService.switchWorkDay(1L);

        assertNotNull(responseDto);
        assertEquals(1L, responseDto.getUserId());
        assertNotNull(responseDto.getEndTime());
    }
    @Test
    void testGetAllLogs_userExists() {
        when(userClient.userExists(1L)).thenReturn(true);

        List<WorkLogResponseDto> logs = workLogService.getAllLogsByUserId(1L);
        assertNotNull(logs);
        assertFalse(logs.isEmpty());
    }
    @Test
    void testStartWorkDay_userNotExists() {
        when(userClient.userExists(99L)).thenReturn(false);

        Exception exception = assertThrows(RuntimeException.class,
                () -> workLogService.switchWorkDay(99L));

        assertTrue(exception.getMessage().contains("User with id 99 was not found"));
    }
}
