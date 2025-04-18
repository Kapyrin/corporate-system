package com.example.timetrackingservice.service;

import com.example.timetrackingservice.dto.WorkLogResponseDto;

import java.time.LocalDateTime;
import java.util.List;

public interface WorkLogCoreService {
    WorkLogResponseDto switchWorkDay(Long userId);

    WorkLogResponseDto getWorkLogById(Long id);

    List<WorkLogResponseDto> getAllLogsByUserId(Long userId);

    List<WorkLogResponseDto> getWorkLogsByDateRange(LocalDateTime from, LocalDateTime to);

    void deleteWorkLogById(Long id);
}
