package ru.kapyrin.telegrambot.dto;

import lombok.Data;

@Data
public class WorkLogReportDto {
    private Long userId;
    private String date;
    private String totalWorked;
    private long daysWorked;
    private long overtimeHours;
}
