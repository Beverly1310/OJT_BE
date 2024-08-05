package com.example.ojt.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
public class ExperienceCandidate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String company;
    private String position;
    @Column(name = "start_at", columnDefinition = "VARCHAR(20)")
    private String startAt;
    @Column(name = "end_at", columnDefinition = "VARCHAR(20)")
    private String endAt;
    private String info;
    private int status;

    @ManyToOne
    @JoinColumn(name = "candidate_id")
    private Candidate candidate;
}
