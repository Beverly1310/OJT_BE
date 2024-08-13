package com.example.ojt.model.dto.response;

import lombok.*;

import java.sql.Timestamp;

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
}
