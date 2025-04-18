package com.example.timetrackingservice.service.impl;

import com.example.timetrackingservice.dto.WorkLogDetailedDayDto;
import com.example.timetrackingservice.dto.WorkLogReportDto;
import com.example.timetrackingservice.entity.WorkLog;
import com.example.timetrackingservice.service.WorkLogReportService;
import com.example.timetrackingservice.service.logic.WorkLogHelperService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WorkLogReportServiceImpl implements WorkLogReportService {
    private final WorkLogHelperService helper;

    @Override
    public WorkLogReportDto getReport(Long userId, String date) {
        List<WorkLog> logs = helper.resolveValidLogs(userId, date);

        Duration total = helper.calculateTotalWorked(logs);
        int daysWorked = logs.size();
        long overtime = helper.calculateOvertime(total, daysWorked);

        return WorkLogReportDto.builder()
                .userId(userId)
                .date(date)
                .totalWorked(total)
                .daysWorked(daysWorked)
                .overtimeHours(overtime)
                .build();
    }

    @Override
    public List<WorkLogDetailedDayDto> getDetailedReport(Long userId, String date) {
        List<WorkLog> logs = helper.resolveValidLogs(userId, date);

        Map<LocalDate, List<WorkLog>> logsByDay = logs.stream()
                .collect(Collectors.groupingBy(log -> log.getStartTime().toLocalDate()));


        return logsByDay.keySet().stream()
                .map(dateKey -> helper.mapToDetailedDtoForDay(dateKey, logsByDay.get(dateKey)))
                .toList();
    }


}
