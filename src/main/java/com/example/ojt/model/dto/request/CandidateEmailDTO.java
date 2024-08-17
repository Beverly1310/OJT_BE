package com.example.ojt.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CandidateEmailDTO {
    private Integer id;
    private String name;
    private String accountEmail;
    private Date birthday;
    private String address;
    private String phone;
    private Integer status;
    private Boolean gender;
    private String linkLinkedin;
    private String linkGit;
    private String position;
    private Integer outstanding;
}
