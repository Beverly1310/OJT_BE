package com.example.ojt.model.dto.response;

import com.example.ojt.model.entity.Account;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CandidateInfoRes {
    private Integer id;
    private String name;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date birthday;
    private String address;
    private String phone;
    private String linkLinkedin;
    private String linkGit;
    private String position;
    private String avatar;
    private String aboutme;
    private String email;
    private List<String> skills;
    private List<String> exp;
    private List<String> edu;
    private List<String> project;
    private List<String> certi;
}
