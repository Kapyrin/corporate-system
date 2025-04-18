package com.example.timetrackingservice.service.impl;

import com.example.timetrackingservice.dto.WorkLogResponseDto;
import com.example.timetrackingservice.entity.WorkLog;
import com.example.timetrackingservice.exception.WorkLogException;
import com.example.timetrackingservice.mapper.WorkLogMapper;
import com.example.timetrackingservice.repository.WorkLogRepo;
import com.example.timetrackingservice.service.WorkLogCoreService;
import com.example.timetrackingservice.service.logic.WorkLogHelperService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WorkLogCoreServiceImpl implements WorkLogCoreService {
    private final WorkLogMapper mapper;
    private final WorkLogRepo repo;
    private final WorkLogHelperService helper;

    @Override
    public WorkLogResponseDto switchWorkDay(Long userId) {
        helper.validateUserExists(userId);

        WorkLog last = repo.findTop1ByUserIdOrderByStartTimeDesc(userId).orElse(null);
        if (last != null) {
            helper.autoClosePreviousShiftIfExpired(last);

            if (last.getEndTime() == null) {
                last.setEndTime(LocalDateTime.now());
                return mapper.toDto(repo.save(last));
            }
        }

        WorkLog newLog = new WorkLog();
        newLog.setUserId(userId);
        newLog.setStartTime(LocalDateTime.now());
        return mapper.toDto(repo.save(newLog));
    }


    @Override
    public WorkLogResponseDto getWorkLogById(Long id) {
        return mapper.toDto(
                repo.findById(id).orElseThrow(() -> new WorkLogException("No work log found with ID: " + id))
        );
    }

    @Override
    public List<WorkLogResponseDto> getAllLogsByUserId(Long userId) {
        helper.validateUserExists(userId);
        return mapper.toDto(repo.findAllByUserId(userId));
    }

    @Override
    public List<WorkLogResponseDto> getWorkLogsByDateRange(LocalDateTime from, LocalDateTime to) {
        return mapper.toDto(repo.findAllByStartTimeBetween(from, to));
    }

    @Override
    public void deleteWorkLogById(Long id) {
        repo.deleteById(id);
    }
}

