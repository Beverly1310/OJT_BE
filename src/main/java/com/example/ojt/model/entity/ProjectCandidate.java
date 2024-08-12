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
public class ProjectCandidate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "name", columnDefinition = "VARCHAR(55)")
    private String name;
    private String link;
    @Column(name = "start_at", columnDefinition = "VARCHAR(20)")
    private Date startAt;
    @Column(name = "end_at", columnDefinition = "VARCHAR(20)")
    private Date endAt;
    private String info;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "candidate_id")
    private Candidate candidate;
}
