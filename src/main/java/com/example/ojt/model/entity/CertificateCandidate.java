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
public class CertificateCandidate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String organization;
    @JsonFormat(pattern = "dd/MM/yyyy")
    @Column(name = "start_at")
    private Date startAt;
    @JsonFormat(pattern = "dd/MM/yyyy")
    @Column(name = "end_at")
    private Date endAt;
    private String info;

    @ManyToOne
    @JoinColumn(name = "candidate_id")
    @JsonIgnore
    private Candidate candidate;
}
