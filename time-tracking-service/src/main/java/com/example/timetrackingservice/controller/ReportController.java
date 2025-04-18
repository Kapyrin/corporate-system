package com.example.timetrackingservice.controller;

import com.example.timetrackingservice.dto.WorkLogDetailedDayDto;
import com.example.timetrackingservice.dto.WorkLogReportDto;
import com.example.timetrackingservice.service.WorkLogReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/report")
@RequiredArgsConstructor
public class ReportController {

    private final WorkLogReportService workLogService;

    @Operation(
            summary = "Get report for user by period",
            description = "Returns total worked time and overtime for the given period. Date formats: yyyy, yyyy-MM, yyyy-MM-nw, yyyy-MM-dd")
    @ApiResponse(responseCode = "200", description = "Report generated")
    @ApiResponse(responseCode = "404", description = "User or logs not found")
    @ApiResponse(responseCode = "400", description = "Invalid report format")
    @GetMapping("/{userId}")
    public ResponseEntity<WorkLogReportDto> getReport(
            @Parameter(name = "userId", description = "User ID") @PathVariable Long userId,
            @Parameter(name = "date", description = "Date format: yyyy / yyyy-MM / yyyy-MM-nw / yyyy-MM-dd") @RequestParam String date) {
        return ResponseEntity.ok(workLogService.getReport(userId, date));
    }

    @Operation(
            summary = "Get detailed report for user by period",
            description = "Returns daily work log entries, total work time and overtime per day.")
    @ApiResponse(responseCode = "200", description = "Report generated")
    @ApiResponse(responseCode = "404", description = "User or logs not found")
    @ApiResponse(responseCode = "400", description = "Invalid report format")

    @GetMapping("/{userId}/detailed")
    public ResponseEntity<List<WorkLogDetailedDayDto>> getDetailedReport(
            @Parameter(name = "userId", description = "User ID") @PathVariable Long userId,
            @Parameter(name = "date", description = "Date format: yyyy / yyyy-MM / yyyy-MM-nw / yyyy-MM-dd") @RequestParam String date) {
        return ResponseEntity.ok(workLogService.getDetailedReport(userId, date));
    }
}

