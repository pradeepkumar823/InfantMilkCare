package com.InfantMilk.Care.service;

import com.InfantMilk.Care.model.ChildProfile;
import com.InfantMilk.Care.model.User;
import com.InfantMilk.Care.repository.ChildProfileRepository;
import com.InfantMilk.Care.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OnboardingService {
    private final UserRepository userRepository;
    private final ChildProfileRepository childProfileRepository;
    private final org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;

    @Transactional
    public User registerUser(String email, String password, String phoneNumber) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException("User already exists");
        }
        User user = User.builder()
                .email(email)
                .password(passwordEncoder.encode(password))
                .phoneNumber(phoneNumber)
                .build();
        return userRepository.save(user);
    }

    @Transactional
    public ChildProfile saveChildDetails(Long userId, ChildProfile childDetails) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        childDetails.setUser(user);
        return childProfileRepository.save(childDetails);
    }
}
