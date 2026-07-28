package com.example.backend.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.dto.DashboardResponseDTO;
import com.example.backend.dto.StatisticsResponseDTO;
import com.example.backend.service.DashboardService;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    // ==========================================
    // Dashboard Summary
    // ==========================================

    @GetMapping
    public ResponseEntity<DashboardResponseDTO> getDashboardSummary() {

        return ResponseEntity.ok(
                dashboardService.getDashboardSummary());
    }

    // ==========================================
    // Document Status Statistics
    // ==========================================

    @GetMapping("/status")
    public ResponseEntity<List<StatisticsResponseDTO>> getStatusStatistics() {

        return ResponseEntity.ok(
                dashboardService.getDocumentsByStatus());
    }

    // ==========================================
    // User Role Statistics
    // ==========================================

    @GetMapping("/roles")
    public ResponseEntity<List<StatisticsResponseDTO>> getRoleStatistics() {

        return ResponseEntity.ok(
                dashboardService.getUsersByRole());
    }

}