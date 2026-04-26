package com.InfantMilk.Care.repository;

import com.InfantMilk.Care.model.FeedingAlarm;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface FeedingAlarmRepository extends JpaRepository<FeedingAlarm, Long> {
    List<FeedingAlarm> findByChildId(Long childId);
}
