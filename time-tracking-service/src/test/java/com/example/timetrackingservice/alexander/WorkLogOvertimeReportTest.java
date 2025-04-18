package com.example.timetrackingservice.alexander;

import com.example.timetrackingservice.client.UserClient;
import com.example.timetrackingservice.dto.WorkLogReportDto;
import com.example.timetrackingservice.service.WorkLogReportService;
import com.example.timetrackingservice.vladimir.AbstractIntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

public class WorkLogOvertimeReportTest extends AbstractIntegrationTest {
    @Autowired
    private WorkLogReportService workLogService;

    @MockBean
    private UserClient userClient;

    @BeforeEach
    void setUp() {
        when(userClient.userExists(anyLong())).thenReturn(true);
    }

    @Test
    void shouldReturnCorrectReportForUserWithOvertimeByYear() {
        Long userId = 1L;
        WorkLogReportDto report = workLogService.getReport(userId, "2025");

        assertNotNull(report);
        assertEquals(userId, report.getUserId());
        assertEquals("2025", report.getDate());
        assertEquals(3, report.getDaysWorked());

        assertEquals(Duration.ofHours(25).plusMinutes(30), report.getTotalWorked());

        assertEquals(1, report.getOvertimeHours());
    }

    @Test
    void shouldReturnCorrectReportForUserWithOvertimeByMonth() {
        Long userId = 1L;
        WorkLogReportDto report = workLogService.getReport(userId, "2025-01");

        assertNotNull(report);
        assertEquals(userId, report.getUserId());
        assertEquals("2025-01", report.getDate());
        assertEquals(3, report.getDaysWorked());

        assertEquals(Duration.ofHours(25).plusMinutes(30), report.getTotalWorked());

        assertEquals(1, report.getOvertimeHours());
    }

    @Test
    void shouldReturnEmptyReportWhenUserExistsButHasNoWorkLogs() {
        Long userId = 0L;

        WorkLogReportDto report = workLogService.getReport(userId, "2025");

        assertNotNull(report);
        assertEquals(userId, report.getUserId());
        assertEquals("2025", report.getDate());
        assertEquals(0, report.getDaysWorked());
        assertEquals(Duration.ZERO, report.getTotalWorked());
        assertEquals(0, report.getOvertimeHours());
    }
}
