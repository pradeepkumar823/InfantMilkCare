package com.InfantMilk.Care.controller;

import com.InfantMilk.Care.model.ChildProfile;
import com.InfantMilk.Care.model.User;
import com.InfantMilk.Care.repository.ChildProfileRepository;
import com.InfantMilk.Care.repository.UserRepository;
import com.InfantMilk.Care.service.SessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final SessionService sessionService;
    private final UserRepository userRepository;
    private final ChildProfileRepository childProfileRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Update Mama's account — phone number and optionally password.
     */
    @PostMapping("/update-user")
    public String updateUser(
            @RequestParam(required = false) String phoneNumber,
            @RequestParam(required = false) String newPassword,
            @RequestParam(required = false) String confirmPassword,
            RedirectAttributes redirectAttributes) {

        User user = sessionService.getCurrentUser();
        if (user == null) return "redirect:/login.html";

        if (phoneNumber != null && !phoneNumber.isBlank()) {
            user.setPhoneNumber(phoneNumber.trim());
        }

        if (newPassword != null && !newPassword.isBlank()) {
            if (newPassword.equals(confirmPassword)) {
                user.setPassword(passwordEncoder.encode(newPassword));
            } else {
                redirectAttributes.addFlashAttribute("profileError", "Passwords do not match.");
                return "redirect:/profile";
            }
        }

        userRepository.save(user);
        redirectAttributes.addFlashAttribute("profileSuccess", "Account updated successfully!");
        return "redirect:/profile";
    }

    /**
     * Update the currently selected child's profile details.
     */
    @PostMapping("/update-child")
    public String updateChild(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer ageMonths,
            @RequestParam(required = false) Double weightKg,
            @RequestParam(required = false) String gender,
            @RequestParam(required = false) String countryRegion,
            @RequestParam(required = false) String privacyShieldEnabled,
            RedirectAttributes redirectAttributes) {

        User user = sessionService.getCurrentUser();
        if (user == null) return "redirect:/login.html";

        ChildProfile child = sessionService.getSelectedChild();
        if (child == null || !child.getUser().getId().equals(user.getId())) {
            redirectAttributes.addFlashAttribute("profileError", "No child profile selected.");
            return "redirect:/profile";
        }

        if (name != null && !name.isBlank()) child.setName(name.trim());
        if (ageMonths != null) child.setAgeMonths(ageMonths);
        if (weightKg != null) child.setWeightKg(weightKg);
        if (gender != null && !gender.isBlank()) child.setGender(gender);
        if (countryRegion != null && !countryRegion.isBlank()) child.setCountryRegion(countryRegion);
        child.setPrivacyShieldEnabled("true".equalsIgnoreCase(privacyShieldEnabled));

        childProfileRepository.save(child);
        redirectAttributes.addFlashAttribute("profileSuccess", "Child profile updated successfully!");
        return "redirect:/profile";
    }
}
