package com.InfantMilk.Care.controller;

import com.InfantMilk.Care.model.ChildProfile;
import com.InfantMilk.Care.model.FeedingAlarm;
import com.InfantMilk.Care.model.FeedingLog;

import com.InfantMilk.Care.repository.FeedingAlarmRepository;
import com.InfantMilk.Care.repository.FeedingLogRepository;
import com.InfantMilk.Care.service.SessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/api/feeding")
@RequiredArgsConstructor
public class FeedingController {
    private final FeedingLogRepository feedingLogRepository;
    private final FeedingAlarmRepository feedingAlarmRepository;
    private final SessionService sessionService;

    @PostMapping("/logs")
    public String addLog(@ModelAttribute FeedingLog log) {
        ChildProfile child = sessionService.getSelectedChild();
        if (child == null) return "redirect:/child-details";

        log.setChild(child);
        log.setTimestamp(LocalDateTime.now());
        feedingLogRepository.save(log);
        
        return "redirect:/dashboard";
    }

    @PostMapping("/alarms")
    public String addAlarm(@ModelAttribute FeedingAlarm alarm) {
        ChildProfile child = sessionService.getSelectedChild();
        if (child == null) return "redirect:/child-details";

        alarm.setChild(child);
        alarm.setEnabled(true);
        feedingAlarmRepository.save(alarm);
        
        return "redirect:/alarms";
    }

    @PostMapping("/logs/{id}/delete")
    public String deleteLog(@PathVariable Long id) {
        ChildProfile child = sessionService.getSelectedChild();
        if (child != null) {
            FeedingLog log = feedingLogRepository.findById(id).orElse(null);
            if (log != null && log.getChild().getId().equals(child.getId())) {
                feedingLogRepository.deleteById(id);
            }
        }
        return "redirect:/history";
    }

    @PostMapping("/alarms/{id}/delete")
    public String deleteAlarm(@PathVariable Long id) {
        ChildProfile child = sessionService.getSelectedChild();
        if (child != null) {
            FeedingAlarm alarm = feedingAlarmRepository.findById(id).orElse(null);
            if (alarm != null && alarm.getChild().getId().equals(child.getId())) {
                feedingAlarmRepository.deleteById(id);
            }
        }
        return "redirect:/alarms";
    }
}
