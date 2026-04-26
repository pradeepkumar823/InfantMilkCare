package com.InfantMilk.Care.controller;

import com.InfantMilk.Care.dto.ChildDetailsRequest;
import com.InfantMilk.Care.dto.RegistrationRequest;
import com.InfantMilk.Care.model.ChildProfile;
import com.InfantMilk.Care.model.User;
import com.InfantMilk.Care.service.OnboardingService;
import com.InfantMilk.Care.service.SessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/api/onboarding")
@RequiredArgsConstructor
public class OnboardingController {
    private final OnboardingService onboardingService;
    private final SessionService sessionService;

    @PostMapping("/register")
    public String register(@ModelAttribute RegistrationRequest request) {
        try {
            onboardingService.registerUser(request.getEmail(), request.getPassword(), request.getPhoneNumber());
            return "redirect:/login.html?registered";
        } catch (Exception e) {
            return "redirect:/register.html?error";
        }
    }

    @PostMapping("/child-details")
    public String saveChildDetails(
            @ModelAttribute ChildDetailsRequest request,
            @RequestParam(value = "privacy", required = false, defaultValue = "false") boolean privacy
            ) {
        
        User user = sessionService.getCurrentUser();
        if (user == null) {
            return "redirect:/login.html";
        }

        ChildProfile profile = ChildProfile.builder()
                .name(request.getName())
                .ageMonths(request.getAgeMonths())
                .weightKg(request.getWeightKg())
                .gender(request.getGender())
                .countryRegion(request.getCountryRegion())
                .privacyShieldEnabled(privacy)
                .build();
                
        ChildProfile savedChild = onboardingService.saveChildDetails(user.getId(), profile);
        sessionService.setSelectedChild(savedChild.getId());
        return "redirect:/dashboard";
    }
}
