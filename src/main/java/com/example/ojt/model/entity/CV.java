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
public class CV {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String url;
    private String fileName;
    @ManyToOne
    @JoinColumn(name = "candidate_id")
    private Candidate candidate;
    private boolean status;
    private Date createdAt;
}
