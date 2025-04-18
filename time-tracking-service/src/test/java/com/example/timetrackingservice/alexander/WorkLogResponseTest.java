package com.example.timetrackingservice.alexander;

import com.example.timetrackingservice.client.UserClient;
import com.example.timetrackingservice.dto.WorkLogResponseDto;
import com.example.timetrackingservice.service.WorkLogCoreService;
import com.example.timetrackingservice.vladimir.AbstractIntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class WorkLogResponseTest extends AbstractIntegrationTest {
    @Autowired
    private WorkLogCoreService workLogService;
    @MockBean
    private UserClient userClient;
    private final Long EXISTS_USER_ID = 1L;
    private final Long NULL_USER_ID = 0L;

    @BeforeEach
    void setUp() {
        when(userClient.userExists(NULL_USER_ID)).thenReturn(false);
        when(userClient.userExists(EXISTS_USER_ID)).thenReturn(true);
    }

    @Test
    void testStartWorkLog() {
        WorkLogResponseDto responseDto = workLogService.switchWorkDay(EXISTS_USER_ID);

        assertNotNull(responseDto);
        assertEquals(EXISTS_USER_ID, responseDto.getUserId());
        assertNotNull(responseDto.getStartTime());
    }

    @Test
    void testStopWorkLog() {
        workLogService.switchWorkDay(EXISTS_USER_ID);
        WorkLogResponseDto responseDto = workLogService.switchWorkDay(EXISTS_USER_ID);

        assertNotNull(responseDto);
        assertEquals(EXISTS_USER_ID, responseDto.getUserId());
        assertNotNull(responseDto.getStartTime());
        assertNotNull(responseDto.getEndTime());
    }

    @Test
    void testGetAllLogs_userExists() {

        List<WorkLogResponseDto> logs = workLogService.getAllLogsByUserId(EXISTS_USER_ID);
        assertNotNull(logs);
        assertFalse(logs.isEmpty());
    }

    @Test
    void testStartWorkDay_userNotExists() {

        Exception exception = assertThrows(RuntimeException.class,
                () -> workLogService.switchWorkDay(NULL_USER_ID));

        assertTrue(exception.getMessage().contains("User with id " + NULL_USER_ID + " was not found"));
    }
}