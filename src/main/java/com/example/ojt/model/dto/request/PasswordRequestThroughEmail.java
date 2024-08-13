package com.example.ojt.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PasswordRequestThroughEmail {
    @NotBlank(message = "You haven't filled an email address yet!")
    @NotEmpty( message = "You haven't filled an email address yet!")
    private String email;
    @NotBlank(message = "You haven't chosen a role yet!")
    @NotEmpty(message = "You haven't chosen a role yet!")
    private String role;
}
