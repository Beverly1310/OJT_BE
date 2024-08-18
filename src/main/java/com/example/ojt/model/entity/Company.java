package com.example.ojt.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String logo;
    private String website;
    private String linkFacebook;
    private String linkLinkedin;
    private Integer followers;
    private Integer size;
    private Integer Outstanding;
    @Column(columnDefinition = "LONGTEXT")
    private String description;
    private String phone;
    private String emailCompany;
    @Column(columnDefinition = "LONGTEXT")
    private String policy;
    private Date createdAt;
    private Date updatedAt;

    @ManyToOne
    @JoinColumn(name = "typeCompany_id")
    private TypeCompany typeCompany;

    @OneToOne  // Fetching Account eagerly
    @JoinColumn(name = "account_id")
    private Account account;

    @OneToMany(mappedBy = "company")
    @JsonIgnore
    private Set<AddressCompany> addressCompanySet;
}
