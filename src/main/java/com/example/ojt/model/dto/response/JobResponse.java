package com.example.ojt.model.dto.response;

import com.example.ojt.model.entity.TypeJob;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobResponse {
    private Integer id;
    private String title;
    private String description;
    private String requirements;
    private String salary;
    private String expireAt;
    private Timestamp createdAt;
    private int status;
    private String companyName;
    private String address;
    private String city;
    private String companyLogo;
    private Set<String> typeJob;
    private Set<String> levelJob;
}
