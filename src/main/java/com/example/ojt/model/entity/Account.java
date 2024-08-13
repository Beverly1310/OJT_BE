package com.example.ojt.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String email;
    private String password;
    private String backupPassword;
    private Integer status;
    @JsonIgnore
    @OneToOne(mappedBy = "account")
    private Company company;
    @JsonIgnore
    @OneToOne(mappedBy = "account")
    private Candidate candidate;
    private Integer otp;
    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;
}
