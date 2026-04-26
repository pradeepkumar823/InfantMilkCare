package com.InfantMilk.Care.repository;

import com.InfantMilk.Care.model.FeedingLog;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface FeedingLogRepository extends JpaRepository<FeedingLog, Long> {
    List<FeedingLog> findByChildIdOrderByTimestampDesc(Long childId);
}
