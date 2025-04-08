package com.example.timetrackingservice.controller;

import com.example.timetrackingservice.dto.WorkLogResponseDto;
import com.example.timetrackingservice.service.WorkLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/time")
@RequiredArgsConstructor
public class TimeTrackRestController {
    private final WorkLogService workLogService;

    @Operation(summary = "Start wor day")
    @ApiResponse(responseCode = "201", description = "Work day started")
    @ApiResponse(responseCode = "409", description = "Work day day can`t be started")
    @PostMapping("/start/{userId}")
    public ResponseEntity<WorkLogResponseDto> startWorkDay(@PathVariable("userId") Long userId) {
        WorkLogResponseDto responseDto = workLogService.startWorkDay(userId);
        return ResponseEntity.status(201).body(responseDto);
    }

    @Operation(summary = "End work day")
    @ApiResponse(responseCode = "201", description = "Work day ended")
    @ApiResponse(responseCode = "409", description = "Work day can`t be ended")
    @PostMapping("/end/{userId}")
    public ResponseEntity<WorkLogResponseDto> endWorkDay(@PathVariable("userId") Long userId) {
        WorkLogResponseDto responseDto = workLogService.endWorkDay(userId);
        return ResponseEntity.status(201).body(responseDto);
    }

}
