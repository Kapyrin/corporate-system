package com.example.timetrackingservice.dto;

import com.example.timetrackingservice.service.logic.MyDurationSerializer;
import com.example.timetrackingservice.service.logic.enity.DateRangeWithDuration;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;

public record WorkLogDetailedDayDto(
        LocalDate date,
        List<DateRangeWithDuration> entries,
        @JsonSerialize(using = MyDurationSerializer.class)
        Duration totalWorked,
        long overtimeHours
) {}
