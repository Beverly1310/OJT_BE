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
public class AddressCompanyRequest {
    private Integer id;
    @NotEmpty(message = "Please fill address!")
    private String address;
    @NotEmpty(message = "Please fill mapUrl!")
    private String mapUrl;
    @NotNull(message = "Please fill location!")
    private Integer location;
}
