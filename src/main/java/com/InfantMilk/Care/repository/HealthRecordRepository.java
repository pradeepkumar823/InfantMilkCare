package com.InfantMilk.Care.repository;

import com.InfantMilk.Care.model.HealthRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface HealthRecordRepository extends JpaRepository<HealthRecord, Long> {
    List<HealthRecord> findByChildIdOrderByRecordedAtDesc(Long childId);
}
