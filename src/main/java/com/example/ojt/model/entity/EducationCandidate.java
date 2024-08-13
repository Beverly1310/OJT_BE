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
public class EducationCandidate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String nameEducation;
    private String major;
    @JsonFormat(pattern = "dd/MM/yyyy")
    @Column(name = "start_at")
    private Date startAt;
    @JsonFormat(pattern = "dd/MM/yyyy")
    @Column(name = "end_at")
    private Date endAt;
    private String info;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "candidate_id")
    private Candidate candidate;
}
