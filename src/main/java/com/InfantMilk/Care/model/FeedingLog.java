package com.InfantMilk.Care.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "feeding_logs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FeedingLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "child_id", nullable = false)
    private ChildProfile child;

    private Double amountMl;
    private String type; // formula, breast milk, etc.
    private LocalDateTime timestamp;
    private String note;
}
