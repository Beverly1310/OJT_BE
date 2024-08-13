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
public class JobCandidates {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private int status;
    private String cvUrl;
    @Column(name = "interview_day", columnDefinition = "VARCHAR(20)")
    private Date interviewDay;
    private String content;

    @ManyToOne
    @JoinColumn(name = "candidate_id")
    private Candidate candidate;
    @ManyToOne
    @JoinColumn(name = "job_id")
    private Job job;
}
