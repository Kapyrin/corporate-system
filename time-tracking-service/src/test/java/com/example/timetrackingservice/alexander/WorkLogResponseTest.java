package com.example.timetrackingservice.alexander;

import com.example.timetrackingservice.client.UserClient;
import com.example.timetrackingservice.dto.WorkLogResponseDto;
import com.example.timetrackingservice.service.WorkLogService;
import com.example.timetrackingservice.vladimir.AbstractIntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

public class WorkLogResponseTest extends AbstractIntegrationTest {
    @Autowired
    private WorkLogService workLogService;
    @MockBean
    private UserClient userClient;
    private final Long EXISTS_USER_ID = 1L;
    private final Long NULL_USER_ID = 0L;

    @BeforeEach
    void setUp() {
        when(userClient.userExists(anyLong())).thenReturn(true);
    }

    @Test
    void testStartWorkLog() {
        WorkLogResponseDto responseDto = workLogService.startWorkDay(EXISTS_USER_ID);

        assertNotNull(responseDto);
        assertEquals(EXISTS_USER_ID, responseDto.getUserId());
        assertNotNull(responseDto.getStartTime());
    }

    @Test
    void testStopWorkLog() {
        workLogService.startWorkDay(EXISTS_USER_ID);
        WorkLogResponseDto responseDto = workLogService.endWorkDay(EXISTS_USER_ID);

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
                () -> workLogService.startWorkDay(NULL_USER_ID));

        assertTrue(exception.getMessage().contains("User with id " + NULL_USER_ID + " was not found"));
    }
}