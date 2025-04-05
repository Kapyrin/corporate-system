package com.example.timetrackingservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;

@Data
@AllArgsConstructor
public class WorkLogDetailDTO {
    private Long id;
    private Long userId;
    private Instant startTime;
    private Instant endTime;
}
