package com.example.ojt.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
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
    @JsonFormat(pattern = "dd/MM/yyyy")
    @Column(name = "start_at")
    private Date startAt;
    @JsonFormat(pattern = "dd/MM/yyyy")
    @Column(name = "end_at")
    private Date endAt;
    private String info;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "candidate_id")
    private Candidate candidate;
}
