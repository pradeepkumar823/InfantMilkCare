package com.InfantMilk.Care.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "health_records")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HealthRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "child_id", nullable = false)
    private ChildProfile child;

    private Double hemoglobinLevel;
    private String hemoglobinStatus; // e.g., "HEALTHY", "LOW"
    private Double weightKg;
    private String allergens; // Comma-separated or use a separate set
    private LocalDateTime recordedAt;
}
