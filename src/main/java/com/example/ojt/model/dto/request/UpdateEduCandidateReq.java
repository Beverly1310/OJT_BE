package com.example.ojt.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UpdateEduCandidateReq {
    private Integer id;
    private String nameEducation;
    private String major;
    private Date startAt;
    private Date endAt;
    private String info;
    private Integer status;
}
