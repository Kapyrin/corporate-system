package com.example.timetrackingservice.service.impl;

import com.example.timetrackingservice.dto.WorkLogReportDto;
import com.example.timetrackingservice.dto.WorkLogResponseDto;
import com.example.timetrackingservice.entity.WorkLog;
import com.example.timetrackingservice.exception.WorkLogException;
import com.example.timetrackingservice.mapper.WorkLogMapper;
import com.example.timetrackingservice.repository.WorkLogRepo;
import com.example.timetrackingservice.service.WorkLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WorkLogServicesImpl implements WorkLogService {
    private final WorkLogRepo repo;
    private final WorkLogMapper mapper;

    @Override
    public WorkLogResponseDto startWorkDay(Long userId) {
        WorkLog lastWorkLog = repo.findLastByUserId(userId).orElseThrow(RuntimeException::new);
        if (lastWorkLog.getEndTime() == null) {
            throw new WorkLogException("Last work log end time is null");
        }
        WorkLog workLog = new WorkLog();
        workLog.setStartTime(LocalDateTime.now());
        workLog.setUserId(userId);
        return mapper.toDto(repo.save(workLog));
    }

    @Override
    public WorkLogResponseDto endWorkDay(Long userId) {
        WorkLog lastWorkLog = repo.findLastByUserId(userId).orElseThrow(RuntimeException::new);
        if (lastWorkLog.getEndTime() != null) {
            throw new WorkLogException("Last work log end time is not null");
        }
        lastWorkLog.setEndTime(LocalDateTime.now());
        return mapper.toDto(repo.save(lastWorkLog));
    }

    @Override
    public WorkLogResponseDto getWorkLogById(Long id) {
        return mapper.toDto(repo.findById(id).orElseThrow(RuntimeException::new));
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
    public WorkLogReportDto getReport(Long userId, LocalDate date) {
        return null;
    }
}
