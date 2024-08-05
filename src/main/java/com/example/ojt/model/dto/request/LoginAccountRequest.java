package com.example.ojt.model.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class LoginAccountRequest {
    @Email(message = "Email should be valid")
    private String email;
    @NotEmpty(message = "Please fill password!")
    @Size(min = 4, max = 12, message = "Password's length must be between 4 and 12")
    private String password;
}
