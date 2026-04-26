package com.InfantMilk.Care.controller;

import com.InfantMilk.Care.model.*;
import com.InfantMilk.Care.repository.FeedingAlarmRepository;
import com.InfantMilk.Care.repository.FeedingLogRepository;
import com.InfantMilk.Care.repository.HealthRecordRepository;
import com.InfantMilk.Care.service.ScannerService;
import com.InfantMilk.Care.service.SessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class WebController {

    private final SessionService sessionService;
    private final FeedingLogRepository feedingLogRepository;
    private final FeedingAlarmRepository feedingAlarmRepository;
    private final HealthRecordRepository healthRecordRepository;
    private final ScannerService scannerService;
    private final com.InfantMilk.Care.service.DashboardService dashboardService;

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping({ "/login.html", "/login" })
    public String login() {
        return "login";
    }

    @GetMapping({ "/register.html", "/register" })
    public String register() {
        return "register";
    }

    @GetMapping("/child-details")
    public String childDetails() {
        return "child-details";
    }

    @PostMapping("/switch-child")
    public String switchChild(@RequestParam Long childId,
            @RequestParam(defaultValue = "/dashboard") String redirectUrl) {
        sessionService.setSelectedChild(childId);
        return "redirect:" + redirectUrl;
    }

    private void addCommonModelAttributes(Model model) {
        User user = sessionService.getCurrentUser();
        if (user != null) {
            model.addAttribute("userName", user.getEmail().split("@")[0]);
            model.addAttribute("children", sessionService.getCurrentUserChildren());
        }

        ChildProfile selectedChild = sessionService.getSelectedChild();
        model.addAttribute("selectedChild", selectedChild);
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        User user = sessionService.getCurrentUser();
        if (user == null)
            return "redirect:/login.html";

        addCommonModelAttributes(model);

        ChildProfile child = sessionService.getSelectedChild();
        if (child == null)
            return "redirect:/child-details";

        List<HealthRecord> records = healthRecordRepository.findByChildIdOrderByRecordedAtDesc(child.getId());
        if (!records.isEmpty()) {
            model.addAttribute("latestRecord", records.get(0));
        }

        List<FeedingLog> logs = feedingLogRepository.findByChildIdOrderByTimestampDesc(child.getId());
        model.addAttribute("feedingLogs", logs);

        List<FeedingAlarm> alarms = feedingAlarmRepository.findByChildId(child.getId());
        model.addAttribute("alarms", alarms);

        if (child.getWeightKg() != null && child.getAgeMonths() != null) {
            model.addAttribute("weightStatus",
                    dashboardService.determineWeightStatus(child.getWeightKg(), child.getAgeMonths()));
        } else {
            model.addAttribute("weightStatus", "UNKNOWN");
        }

        return "dashboard";
    }

    @GetMapping("/history")
    public String history(Model model) {
        if (sessionService.getCurrentUser() == null)
            return "redirect:/login.html";
        addCommonModelAttributes(model);

        ChildProfile child = sessionService.getSelectedChild();
        if (child != null) {
            model.addAttribute("healthRecords",
                    healthRecordRepository.findByChildIdOrderByRecordedAtDesc(child.getId()));
            model.addAttribute("feedingLogs", feedingLogRepository.findByChildIdOrderByTimestampDesc(child.getId()));
        }

        return "history";
    }

    @GetMapping("/alarms")
    public String alarms(Model model) {
        if (sessionService.getCurrentUser() == null)
            return "redirect:/login.html";
        addCommonModelAttributes(model);

        ChildProfile child = sessionService.getSelectedChild();
        if (child != null) {
            model.addAttribute("alarmsList", feedingAlarmRepository.findByChildId(child.getId()));
        }
        return "alarms";
    }

    @GetMapping("/profile")
    public String profile(Model model) {
        User user = sessionService.getCurrentUser();
        if (user == null)
            return "redirect:/login.html";
        addCommonModelAttributes(model);
        model.addAttribute("currentUser", user);
        return "profile";
    }

    @GetMapping("/scanner")
    public String scanner(Model model) {
        if (sessionService.getCurrentUser() == null)
            return "redirect:/login.html";
        addCommonModelAttributes(model);
        return "scanner";
    }

    @PostMapping("/scanner/scan")
    public String performScan(Model model) {
        if (sessionService.getCurrentUser() == null)
            return "redirect:/login.html";
        addCommonModelAttributes(model);

        model.addAttribute("scanResult", scannerService.simulateScan());
        return "scanner";
    }
}
