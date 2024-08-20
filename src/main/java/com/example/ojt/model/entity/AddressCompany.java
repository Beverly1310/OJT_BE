package com.example.ojt.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
public class AddressCompany {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String address;
    @Column(columnDefinition = "LONGTEXT")
    private String mapUrl;
    @Column(name = "created_at")
    private Date createdAt;
    private int status;

    @ManyToOne
    @JoinColumn(name = "company_id")

    private Company company;

    @ManyToOne
    @JoinColumn(name = "location_id")

    private Location location;
}
