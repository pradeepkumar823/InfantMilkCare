package com.InfantMilk.Care.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "child_profiles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChildProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String name;
    private Integer ageMonths;
    private Double weightKg;
    private String gender;
    private String countryRegion;
    private Boolean privacyShieldEnabled;

    @OneToMany(mappedBy = "child", cascade = CascadeType.ALL)
    private List<FeedingLog> feedingLogs;

    @OneToMany(mappedBy = "child", cascade = CascadeType.ALL)
    private List<FeedingAlarm> alarms;

    @OneToMany(mappedBy = "child", cascade = CascadeType.ALL)
    private List<HealthRecord> healthRecords;
}
