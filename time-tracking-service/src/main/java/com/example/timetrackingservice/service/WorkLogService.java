package com.example.timetrackingservice.service;

import com.example.timetrackingservice.dto.WorkLogReportDto;
import com.example.timetrackingservice.dto.WorkLogResponseDto;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public interface WorkLogService {

    WorkLogResponseDto startWorkDay(Long userId);

    WorkLogResponseDto endWorkDay(Long userId);

    WorkLogResponseDto getWorkLogById(Long id);

    List<WorkLogResponseDto> getAllLogsByUserId(Long userId);

    void deleteWorkLogById(Long id);

    List<WorkLogResponseDto> getWorkLogsByDateRange(LocalDateTime from, LocalDateTime to);

    WorkLogReportDto getReport(Long userId, String date);
}
