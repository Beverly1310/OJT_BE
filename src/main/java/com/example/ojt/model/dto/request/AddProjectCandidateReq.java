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
public class AddProjectCandidateReq {
    @NotNull(message = "Name of project must not be empty")
    @NotBlank(message = "Name of project must not be empty")
    private String name;
    @NotNull(message = "Information of project must not be empty")
    @NotBlank(message = "Information of project must not be empty")
    private String info;
    @NotNull(message = "Link of project must not be empty")
    @NotBlank(message = "Link of project must not be empty")
    private String link;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startAt;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endAt;
}
