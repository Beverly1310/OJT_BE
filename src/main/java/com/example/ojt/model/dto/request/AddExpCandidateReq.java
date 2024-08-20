package com.example.ojt.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class AddExpCandidateReq {
    @NotBlank(message = "Position must not be blank")
    @NotNull(message = "Position must not be null")
    private String position;
    @NotBlank(message = "Company must not be blank")
    @NotNull(message = "Company must not be null")
    private String company;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startAt;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endAt;
    @NotBlank(message = "Information must not be blank")
    @NotNull(message = "Information must not be null")
    private String info;
}
