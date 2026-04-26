package com.InfantMilk.Care.repository;

import com.InfantMilk.Care.model.ChildProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ChildProfileRepository extends JpaRepository<ChildProfile, Long> {
    List<ChildProfile> findByUserId(Long userId);
}
