package com.InfantMilk.Care.controller;

import com.InfantMilk.Care.model.ChildProfile;
import com.InfantMilk.Care.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {
    private final DashboardService dashboardService;

    @GetMapping("/{childId}")
    public ResponseEntity<ChildProfile> getDashboard(@PathVariable Long childId) {
        return ResponseEntity.ok(dashboardService.getChildDashboardData(childId));
    }

    @GetMapping("/{childId}/weight-status")
    public ResponseEntity<String> getWeightStatus(@PathVariable Long childId) {
        ChildProfile child = dashboardService.getChildDashboardData(childId);
        return ResponseEntity.ok(dashboardService.determineWeightStatus(child.getWeightKg(), child.getAgeMonths()));
    }
}
