package com.InfantMilk.Care.service;

import com.InfantMilk.Care.model.ChildProfile;
import com.InfantMilk.Care.repository.ChildProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DashboardService {
    private final ChildProfileRepository childProfileRepository;

    public String determineWeightStatus(Double weight, Integer ageMonths) {
        if (weight == null || ageMonths == null) return "UNKNOWN";
        
        if (ageMonths <= 12) {
            if (weight < 7.0) return "UNDERWEIGHT";
            if (weight > 12.0) return "OVERWEIGHT";
        }
        return "NORMAL";
    }

    public String determineHbStatus(Double hemoglobinLevel) {
        if (hemoglobinLevel == null) return "UNKNOWN";
        if (hemoglobinLevel < 11.0) return "ANEMIC";
        if (hemoglobinLevel > 14.0) return "HIGH";
        return "HEALTHY";
    }

    public ChildProfile getChildDashboardData(Long childId) {
        return childProfileRepository.findById(childId)
                .orElseThrow(() -> new RuntimeException("Child not found"));
    }
}
