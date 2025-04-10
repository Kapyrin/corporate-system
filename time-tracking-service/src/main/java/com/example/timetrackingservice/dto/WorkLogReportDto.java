package com.example.timetrackingservice.dto;

import com.example.timetrackingservice.service.logic.MyDurationSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkLogReportDto {
    private Long userId;
    private String date;
    @JsonSerialize(using = MyDurationSerializer.class)
    private Duration totalWorked;
    private long daysWorked;
    private long overtimeHours;
}