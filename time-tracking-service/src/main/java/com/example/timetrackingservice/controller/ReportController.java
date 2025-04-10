package com.example.timetrackingservice.controller;

import com.example.timetrackingservice.dto.WorkLogReportDto;
import com.example.timetrackingservice.service.WorkLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/report")
@RequiredArgsConstructor
public class ReportController {

    private final WorkLogService workLogService;

    @Operation(summary = "Get report for user by period")
    @ApiResponse(responseCode = "200", description = "Report generated")
    @ApiResponse(responseCode = "404", description = "User or logs not found")
    @ApiResponse(responseCode = "400", description = "Invalid report format")
    @GetMapping("/{userId}")
    public ResponseEntity<WorkLogReportDto> getReport(
            @Parameter(name = "userId", description = "User ID") @PathVariable("userId") Long userId,
            @Parameter(name = "date", description = "Date format: yyyy for year, yyyy-MM for month, yyyy-MM-nw for week, yyyy-MM-dd for day") @RequestParam String date) {
        return ResponseEntity.ok(workLogService.getReport(userId, date));
    }
}
