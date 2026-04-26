package com.InfantMilk.Care.controller;

import com.InfantMilk.Care.model.ChildProfile;
import com.InfantMilk.Care.model.HealthRecord;
import com.InfantMilk.Care.repository.ChildProfileRepository;
import com.InfantMilk.Care.repository.HealthRecordRepository;
import com.InfantMilk.Care.service.DashboardService;
import com.InfantMilk.Care.service.SessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;

@Controller
@RequestMapping("/api/health")
@RequiredArgsConstructor
public class HealthController {
    private final HealthRecordRepository healthRecordRepository;
    private final ChildProfileRepository childProfileRepository;
    private final DashboardService dashboardService;
    private final SessionService sessionService;

    @PostMapping("/record")
    public String saveRecord(@ModelAttribute HealthRecord record) {
        ChildProfile child = sessionService.getSelectedChild();
        if (child == null) return "redirect:/child-details";
        
        record.setChild(child);
        record.setHemoglobinStatus(dashboardService.determineHbStatus(record.getHemoglobinLevel()));
        
        if (record.getWeightKg() != null) {
            child.setWeightKg(record.getWeightKg()); 
            childProfileRepository.save(child);
        }

        record.setRecordedAt(LocalDateTime.now());
        healthRecordRepository.save(record);
        
        return "redirect:/dashboard";
    }
}
