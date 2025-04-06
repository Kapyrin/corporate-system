package com.example.timetrackingservice.service.impl;

import com.example.timetrackingservice.dto.WorkLogReportDto;
import com.example.timetrackingservice.dto.WorkLogResponseDto;
import com.example.timetrackingservice.entity.WorkLog;
import com.example.timetrackingservice.exception.WorkLogException;
import com.example.timetrackingservice.mapper.WorkLogMapper;
import com.example.timetrackingservice.repository.WorkLogRepo;
import com.example.timetrackingservice.service.WorkLogService;
import com.example.timetrackingservice.service.logic.ReportDateResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WorkLogServicesImpl implements WorkLogService {
    private final WorkLogRepo repo;
    private final WorkLogMapper mapper;
    private final ReportDateResolver reportDateResolver;

    @Override
    public WorkLogResponseDto startWorkDay(Long userId) {
        WorkLog lastWorkLog = repo.findTop1ByUserIdOrderByStartTimeDesc(userId).orElse(null);

        if (lastWorkLog != null) {
            autoClosePreviousShiftIfExpired(lastWorkLog);
            if (lastWorkLog.getEndTime() == null) {
                throw new WorkLogException("Previous shift is still open.");
            }
        }

        WorkLog workLog = new WorkLog();
        workLog.setStartTime(LocalDateTime.now());
        workLog.setUserId(userId);
        return mapper.toDto(repo.save(workLog));
    }

    @Override
    public WorkLogResponseDto endWorkDay(Long userId) {
        WorkLog lastWorkLog = repo.findTop1ByUserIdOrderByStartTimeDesc(userId)
                .orElseThrow(() -> new WorkLogException("No work log found for user ID: " + userId));

        autoClosePreviousShiftIfExpired(lastWorkLog);

        if (lastWorkLog.getEndTime() != null) {
            throw new WorkLogException("Work day already ended.");
        }

        lastWorkLog.setEndTime(LocalDateTime.now());
        return mapper.toDto(repo.save(lastWorkLog));
    }

    @Override
    public WorkLogResponseDto getWorkLogById(Long id) {
        return mapper.toDto(repo.findById(id)
                .orElseThrow(() -> new WorkLogException("No work log found with ID: " + id)));
    }

    @Override
    public List<WorkLogResponseDto> getAllLogsByUserId(Long userId) {
        return mapper.toDto(repo.findAllByUserId(userId));
    }

    @Override
    public void deleteWorkLogById(Long id) {
        repo.deleteById(id);
    }

    @Override
    public List<WorkLogResponseDto> getWorkLogsByDateRange(LocalDateTime from, LocalDateTime to) {
        return mapper.toDto(repo.findAllByStartTimeBetween(from, to));
    }

    @Override
    public WorkLogReportDto getReport(Long userId, String date) {
        return null;
    }

    private void autoClosePreviousShiftIfExpired(WorkLog lastWorkLog) {
        if (lastWorkLog.getEndTime() == null &&
                lastWorkLog.getStartTime().isBefore(LocalDateTime.now().minusHours(23))) {

            lastWorkLog.setEndTime(lastWorkLog.getStartTime().plusHours(8));
            repo.save(lastWorkLog);
        }
    }
}
