package com.example.timetrackingservice.dto;

import com.example.timetrackingservice.service.logic.MyDurationSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
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
    @JsonSerialize(using = MyDurationSerializer.class)

    private Duration totalWorked;
    private long daysWorked;
    private long overtimeHours;
}