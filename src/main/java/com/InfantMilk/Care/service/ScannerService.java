package com.InfantMilk.Care.service;

import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class ScannerService {
    
    private final Random random = new Random();

    @Data
    public static class ScanResult {
        private boolean isSafe;
        private String title;
        private String message;
    }

    public ScanResult simulateScan() {
        ScanResult result = new ScanResult();
        boolean safe = random.nextDouble() > 0.3; // 70% chance of being safe
        
        result.setSafe(safe);
        if (safe) {
            result.setTitle("Safe Concentration");
            result.setMessage("No harmful additives detected for age group 6-12m. BIS Compliant.");
        } else {
            result.setTitle("Allergen Detected!");
            result.setMessage("Detected: Excessive Soy & Preservatives (BIS Alert)");
        }
        return result;
    }
}
