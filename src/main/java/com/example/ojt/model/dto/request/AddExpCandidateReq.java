package com.example.ojt.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    private Date startAt;
    private Date endAt;
    @NotBlank(message = "Information must not be blank")
    @NotNull(message = "Information must not be null")
    private String info;
}
