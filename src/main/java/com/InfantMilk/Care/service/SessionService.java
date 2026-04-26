package com.InfantMilk.Care.service;

import com.InfantMilk.Care.model.ChildProfile;
import com.InfantMilk.Care.model.User;
import com.InfantMilk.Care.repository.ChildProfileRepository;
import com.InfantMilk.Care.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SessionService {
    private final UserRepository userRepository;
    private final ChildProfileRepository childProfileRepository;
    private final HttpSession httpSession;

    public User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        if (email == null || email.equals("anonymousUser")) {
            return null;
        }
        return userRepository.findByEmail(email).orElse(null);
    }

    public List<ChildProfile> getCurrentUserChildren() {
        User user = getCurrentUser();
        if (user == null) return List.of();
        return childProfileRepository.findByUserId(user.getId());
    }

    public ChildProfile getSelectedChild() {
        Long childId = (Long) httpSession.getAttribute("selectedChildId");
        if (childId != null) {
            ChildProfile child = childProfileRepository.findById(childId).orElse(null);
            if (child != null) return child;
        }
        
        // If nothing is selected or found, select the first one by default if they have any
        List<ChildProfile> children = getCurrentUserChildren();
        if (!children.isEmpty()) {
            ChildProfile child = children.get(0);
            setSelectedChild(child.getId());
            return child;
        }
        return null;
    }

    public void setSelectedChild(Long childId) {
        // Validate ownership before setting
        User user = getCurrentUser();
        if (user != null) {
            ChildProfile child = childProfileRepository.findById(childId).orElse(null);
            if (child != null && child.getUser().getId().equals(user.getId())) {
                httpSession.setAttribute("selectedChildId", childId);
            }
        }
    }
}
