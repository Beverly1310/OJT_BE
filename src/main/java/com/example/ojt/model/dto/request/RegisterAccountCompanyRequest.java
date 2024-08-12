package com.example.ojt.model.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class RegisterAccountCompanyRequest {

    @NotEmpty(message = "Please fill password!")
    @Size(min = 4, max = 12, message = "Password's length must be between 4 and 12")
    private String password;
    @NotBlank(message = "confirmPassword must be not blank")
    private String confirmPassword;
    @NotEmpty(message = "Please fill nameCompany!")
    private String name;
    @NotEmpty(message = "Please fill phone!")
    @Pattern(regexp = "(0[3|5|7|8|9])+([0-9]{8})\\b", message = "Enter the Vietnamese phone")
    private String phone;
    @NotEmpty(message = "Please fill email!")
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "Email should be valid")
    private String emailCompany;
    @NotNull(message = "Location ID cannot be null")
    private Integer locationId;

}
