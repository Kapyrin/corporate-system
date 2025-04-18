package com.example.timetrackingservice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkLogResponseDto {
    private Long id;
    private Long userId;
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm")
    private LocalDateTime startTime;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm")
    private LocalDateTime endTime;
}