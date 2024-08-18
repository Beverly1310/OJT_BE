package com.example.ojt.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class JobAddRequest {
    @NotNull(message = "Title must not be empty")
    @NotBlank(message = "Title must not be empty")
    private String title;

    @NotNull(message = "Description must not be empty")
    @NotBlank(message = "Description must not be empty")
    private String description;

    @NotNull(message = "Requirement must not be empty")
    @NotBlank(message = "Requirement must not be empty")
    private String requirements;

    @NotNull(message = "Salary must not be empty")
    @NotBlank(message = "Salary must not be empty")
    private String salary;

    @NotNull(message = "Expire date must not be empty")
    @NotBlank(message = "Expire date must not be empty")
    private String expireAt;

    @NotNull(message = "Location must not be empty")
    private Integer locationId;

    @NotNull(message = "Level must not be empty")
    private List<Integer> levelJobIds;

    @NotNull(message = "Type job must not be empty")
    private List<Integer> typeJobIds;
}
