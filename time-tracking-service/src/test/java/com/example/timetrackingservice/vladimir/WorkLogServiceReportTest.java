package com.example.timetrackingservice.vladimir;

import com.example.timetrackingservice.client.UserClient;
import com.example.timetrackingservice.dto.WorkLogReportDto;
import com.example.timetrackingservice.service.WorkLogService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

public class WorkLogServiceReportTest extends AbstractIntegrationTest {

    @Autowired
    private WorkLogService workLogService;

    @MockBean
    private UserClient userClient;

    @Test
    void shouldReturnCorrectReportForUserWithOvertime() {
        when(userClient.userExists(anyLong())).thenReturn(true);

        WorkLogReportDto report = workLogService.getReport(1L, "2025");

        assertNotNull(report);
        assertEquals(1L, report.getUserId());
        assertEquals("2025", report.getDate());
        assertEquals(3, report.getDaysWorked());

        Duration expectedTotalWorked = Duration.ofHours(9 + 9 + 7).plusMinutes(30);
        Duration notExpectedTotalWorked = Duration.ofHours(9 + 9 + 7).plusMinutes(45);

        assertEquals(expectedTotalWorked, report.getTotalWorked());
        assertNotEquals(notExpectedTotalWorked, report.getTotalWorked());
        assertEquals(1, report.getOvertimeHours());
    }

    @Test
    void shouldReturnEmptyReportWhenUserExistsButHasNoWorkLogs() {
        Long userId = 99L;

        when(userClient.userExists(userId)).thenReturn(true);

        WorkLogReportDto report = workLogService.getReport(userId, "2025");

        assertNotNull(report);
        assertEquals(userId, report.getUserId());
        assertEquals("2025", report.getDate());
        assertEquals(0, report.getDaysWorked());
        assertEquals(Duration.ZERO, report.getTotalWorked());
        assertEquals(0, report.getOvertimeHours());
    }

}
