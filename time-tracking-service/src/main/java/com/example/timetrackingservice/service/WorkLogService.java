package com.example.timetrackingservice.service;

import com.example.timetrackingservice.dto.WorkLogReportDto;
import com.example.timetrackingservice.dto.WorkLogResponseDto;

import java.time.LocalDate;
import java.util.List;

public interface WorkLogService {

    WorkLogResponseDto startWorkDay(Long userId);

    WorkLogResponseDto endWorkDay(Long userId);

    WorkLogResponseDto getWorkLogById(Long id);

    List<WorkLogResponseDto> getAllLogsByUserId(Long userId);

    void deleteWorkLogById(Long id);

    WorkLogReportDto getReport(Long userId, LocalDate date);
}
