package com.example.timetrackingservice.service.logic;

import com.example.timetrackingservice.client.UserClient;
import com.example.timetrackingservice.dto.WorkLogDetailedDayDto;
import com.example.timetrackingservice.entity.WorkLog;
import com.example.timetrackingservice.exception.UserNotFoundException;
import com.example.timetrackingservice.repository.WorkLogRepo;
import com.example.timetrackingservice.service.logic.enity.DateRange;
import com.example.timetrackingservice.service.logic.enity.DateRangeWithDuration;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WorkLogHelperService {

    private final WorkLogRepo repo;
    private final UserClient userClient;
    private final ReportDateResolver resolver;

    public void validateUserExists(Long userId) {
        if (!Boolean.TRUE.equals(userClient.userExists(userId))) {
            throw new UserNotFoundException(userId);
        }
    }

    public void autoClosePreviousShiftIfExpired(WorkLog workLog) {
        if (workLog.getEndTime() == null &&
                workLog.getStartTime().isBefore(LocalDateTime.now().minusHours(23))) {
            workLog.setEndTime(workLog.getStartTime().plusHours(8));
            repo.save(workLog);
        }
    }

    public List<WorkLog> resolveValidLogs(Long userId, String date) {
        validateUserExists(userId);
        WorkLog last = repo.findTop1ByUserIdOrderByStartTimeDesc(userId).orElse(null);
        if (last != null) {
            autoClosePreviousShiftIfExpired(last);
        }

        DateRange range = resolver.resolveDateRange(date);

        return repo.findAllByUserIdAndStartTimeBetween(userId, range.from(), range.to()).stream()
                .filter(log -> log.getEndTime() != null)
                .collect(Collectors.toList());
    }

    public Duration calculateTotalWorked(List<WorkLog> logs) {
        return logs.stream()
                .map(log -> Duration.between(log.getStartTime(), log.getEndTime()))
                .reduce(Duration.ZERO, Duration::plus);
    }

    public WorkLogDetailedDayDto mapToDetailedDtoForDay(LocalDate date, List<WorkLog> logsForDay) {
        List<DateRangeWithDuration> entries = logsForDay.stream()
                .map(log -> new DateRangeWithDuration(
                        log.getStartTime(),
                        log.getEndTime(),
                        Duration.between(log.getStartTime(), log.getEndTime())))
                .toList();

        Duration totalWorked = calculateTotalWorked(logsForDay);
        long overtime = calculateOvertime(totalWorked);

        return new WorkLogDetailedDayDto(date, entries, totalWorked, overtime);
    }


    public long calculateOvertime(Duration totalWorked, int daysWorked) {
        long expectedHours = daysWorked * 8L;
        long actualHours = totalWorked.toHours();
        return Math.max(0, actualHours - expectedHours);
    }

    public long calculateOvertime(Duration totalWorked) {
        return Math.max(0, totalWorked.toHours() - 8L);
    }
}
