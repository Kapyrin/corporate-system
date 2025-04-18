package com.example.timetrackingservice.service.logic.enity;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record DateRange(
        @JsonFormat(pattern = "HH:mm") LocalDateTime from,
        @JsonFormat(pattern = "HH:mm") LocalDateTime to

) {}
