package com.example.timetrackingservice.dto;

import lombok.*;

import java.time.Duration;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkLogReportDto {
    private Long userId;
    private LocalDate date;
    private Duration totalWorked;
    private boolean overtime;
}