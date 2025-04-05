package com.example.timetrackingservice.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
public class WorkLogCreateDTO {
    @NotNull
    private Long userId;
    @NotNull
    private Instant startTime;
    private Instant endTime;
}
