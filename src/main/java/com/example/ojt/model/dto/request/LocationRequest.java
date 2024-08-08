package com.example.ojt.model.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class LocationRequest {
    @NotEmpty(message = "Please fill Location!")
    private String nameCity;
}
