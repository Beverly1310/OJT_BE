package com.example.ojt.model.dto.response;

import lombok.*;

import java.sql.Timestamp;
import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JobDetailDTO {
    private Integer id;
    private String title;
    private String description;
    private String requirements;
    private String salary;
    private String expireAt;
    private Timestamp createdAt;
    private double outstanding;
    private int status;
    private Set<LevelJobDTO> levelJobs;
    private CompanyDTO company;
    private AddressCompanyDTO addressCompany;
}
