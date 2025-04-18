package com.example.timetrackingservice.vladimir;

import com.example.timetrackingservice.client.UserClient;
import com.example.timetrackingservice.dto.WorkLogResponseDto;
import com.example.timetrackingservice.entity.WorkLog;
import com.example.timetrackingservice.repository.WorkLogRepo;
import com.example.timetrackingservice.service.WorkLogCoreService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class WorkLogAutoCloseTest extends AbstractIntegrationTest {

    @Autowired
    private WorkLogRepo workLogRepo;

    @Autowired
    private WorkLogCoreService workLogService;

    @MockBean
    private UserClient userClient;

    private final Long testUserId = 99L;

    @BeforeEach
    void setup() {
        WorkLog oldLog = new WorkLog();
        oldLog.setUserId(testUserId);
        oldLog.setStartTime(LocalDateTime.now().minusHours(25));
        workLogRepo.save(oldLog);

        Mockito.when(userClient.userExists(testUserId)).thenReturn(true);
    }

    @Test
    void testAutoClosePreviousShift() {
        WorkLogResponseDto newShift = workLogService.switchWorkDay(testUserId);

        Optional<WorkLog> previous = workLogRepo.findAllByUserId(testUserId).stream()
                .filter(log -> !log.getId().equals(newShift.getId()))
                .findFirst();

        assertTrue(previous.isPresent());
        assertNotNull(previous.get().getEndTime());

        LocalDateTime expectedEndTime = previous.get().getStartTime().plusHours(8);
        LocalDateTime notExpectedEndTime = previous.get().getStartTime().plusHours(9);
        assertEquals(expectedEndTime, previous.get().getEndTime());
        assertNotEquals(notExpectedEndTime, previous.get().getEndTime());

        assertNotNull(newShift.getStartTime());
        assertEquals(testUserId, newShift.getUserId());
    }
}
