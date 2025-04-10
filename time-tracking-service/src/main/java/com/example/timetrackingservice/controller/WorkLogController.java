package com.example.timetrackingservice.controller;

import com.example.timetrackingservice.dto.WorkLogResponseDto;
import com.example.timetrackingservice.service.WorkLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/worklog")
@RequiredArgsConstructor
public class WorkLogController {

    private final WorkLogService workLogService;

    @Operation(summary = "Start work day")
    @ApiResponse(responseCode = "201", description = "Work day started")
    @ApiResponse(responseCode = "409", description = "Cannot start work day")
    @PostMapping("/start/{userId}")
    public ResponseEntity<WorkLogResponseDto> startWorkDay(
            @Parameter(name = "userId", description = "User ID") @PathVariable("userId") Long userId) {
        return ResponseEntity.status(201).body(workLogService.startWorkDay(userId));
    }

    @Operation(summary = "End work day")
    @ApiResponse(responseCode = "201", description = "Work day ended")
    @ApiResponse(responseCode = "409", description = "Cannot end work day")
    @PostMapping("/end/{userId}")
    public ResponseEntity<WorkLogResponseDto> endWorkDay(
            @Parameter(name = "userId", description = "User ID") @PathVariable("userId") Long userId) {
        return ResponseEntity.status(201).body(workLogService.endWorkDay(userId));
    }

    @Operation(summary = "Get work log by ID")
    @ApiResponse(responseCode = "200", description = "Work log found")
    @ApiResponse(responseCode = "404", description = "Work log not found")
    @GetMapping("/{id}")
    public ResponseEntity<WorkLogResponseDto> getWorkLogById(
            @Parameter(name = "id", description = "Work log ID") @PathVariable("id") Long id) {
        return ResponseEntity.ok(workLogService.getWorkLogById(id));
    }

    @Operation(summary = "Get all logs by user ID")
    @ApiResponse(responseCode = "200", description = "Logs found")
    @ApiResponse(responseCode = "404", description = "Logs not found")
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<WorkLogResponseDto>> getAllLogsByUserId(
            @Parameter(name = "userId", description = "User ID") @PathVariable("userId") Long userId) {
        return ResponseEntity.ok(workLogService.getAllLogsByUserId(userId));
    }

    @Operation(summary = "Get logs by date range")
    @ApiResponse(responseCode = "200", description = "Logs found")
    @ApiResponse(responseCode = "404", description = "Logs not found")
    @GetMapping
    public ResponseEntity<List<WorkLogResponseDto>> getLogsByDateRange(
            @Parameter(name = "from", description = "Start date-time", example = "2025-01-01T00:00:00") @RequestParam LocalDateTime from,
            @Parameter(name = "to", description = "End date-time", example = "2025-12-31T23:59:59") @RequestParam LocalDateTime to) {
        return ResponseEntity.ok(workLogService.getWorkLogsByDateRange(from, to));
    }

    @Operation(summary = "Delete log by ID")
    @ApiResponse(responseCode = "204", description = "Log deleted")
    @ApiResponse(responseCode = "404", description = "Log not found")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLogById(
            @Parameter(name = "id", description = "Work log ID") @PathVariable("id") Long id) {
        workLogService.deleteWorkLogById(id);
        return ResponseEntity.noContent().build();
    }
}
