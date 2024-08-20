package com.example.ojt.model.dto.request;

import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EduCandidateAddReq {
    @NotBlank(message = "Please fill name of education")
    private String nameEducation;
    @NotBlank(message = "Please fill major")
    private String major;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startAt;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endAt;
    @NotBlank(message = "Please fill information of education")
    private String info;
}
