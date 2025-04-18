package com.example.timetrackingservice.service.logic.enity;

import com.example.timetrackingservice.service.logic.MyDurationSerializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.time.Duration;
import java.time.LocalDateTime;

public record DateRangeWithDuration(
        @JsonFormat(pattern = "HH:mm") LocalDateTime from,
        @JsonFormat(pattern = "HH:mm") LocalDateTime to,
        @JsonSerialize(using = MyDurationSerializer.class) Duration duration
) {}
