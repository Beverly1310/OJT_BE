package com.example.ojt.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AddSkillCandidateReq {
    private String name;
    @NotNull(message = "Level job must not be empty")
    @NotBlank(message = "Level job must not be empty")
    private String levelJobName;
}
