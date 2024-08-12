package com.example.ojt.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    private Date startAt;
    @Column(name = "end_at", columnDefinition = "VARCHAR(20)")
    private Date endAt;
    private String info;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "candidate_id")
    private Candidate candidate;
}
