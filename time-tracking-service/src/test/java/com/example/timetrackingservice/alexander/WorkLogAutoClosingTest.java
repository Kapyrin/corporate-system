package com.example.timetrackingservice.alexander;

import com.example.timetrackingservice.client.UserClient;
import com.example.timetrackingservice.dto.WorkLogResponseDto;
import com.example.timetrackingservice.entity.WorkLog;

import com.example.timetrackingservice.repository.WorkLogRepo;
import com.example.timetrackingservice.service.WorkLogCoreService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.Rollback;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
@Rollback
public class WorkLogAutoClosingTest {
    @Autowired
    private WorkLogRepo workLogRepo;

    @Autowired
    private WorkLogCoreService workLogService;

    @MockBean
    private UserClient userClient;

    private final Long testUserId = 100L;
    private final int TOO_LONG_WORKING_DAY_LENGTH = 25;

    @BeforeEach
    void setup() {

        WorkLog twentyFiveHourWorkLog = new WorkLog();
        twentyFiveHourWorkLog.setUserId(testUserId);
        twentyFiveHourWorkLog.setStartTime(LocalDateTime.now().minusHours(TOO_LONG_WORKING_DAY_LENGTH));
        workLogRepo.save(twentyFiveHourWorkLog);

        Mockito.when(userClient.userExists(testUserId)).thenReturn(true);
    }

    @Test
    void testAutoClosePreviousShiftEightOurs() {
        WorkLogResponseDto newShift = workLogService.switchWorkDay(testUserId);

        WorkLog previous = workLogRepo.findAllByUserId(testUserId).stream()
                .filter(log -> !log.getId().equals(newShift.getId()))
                .findFirst().orElse(null);

        assertNotNull(previous);

        assertEquals(previous.getStartTime().plusHours(8), previous.getEndTime());

        assertNotNull(newShift.getStartTime());
        assertEquals(testUserId, newShift.getUserId());
    }
}
