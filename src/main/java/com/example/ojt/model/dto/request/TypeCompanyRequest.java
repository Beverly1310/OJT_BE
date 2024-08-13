package com.example.ojt.model.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TypeCompanyRequest {
//    @NotNull(message = "Please provide an ID!")
    private Integer id;
    @NotEmpty(message = "Please fill TypeCompany!")
    private String name;
}
