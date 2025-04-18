package com.example.timetrackingservice.service;

import com.example.timetrackingservice.dto.WorkLogDetailedDayDto;
import com.example.timetrackingservice.dto.WorkLogReportDto;

import java.util.List;

public interface WorkLogReportService {
    WorkLogReportDto getReport(Long userId, String date);

    List<WorkLogDetailedDayDto> getDetailedReport(Long userId, String date);
}
