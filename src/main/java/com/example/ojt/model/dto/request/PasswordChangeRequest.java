package com.example.ojt.model.dto.request;

import com.example.ojt.validation.PasswordsMatch;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@PasswordsMatch
public class PasswordChangeRequest {
    @NotEmpty(message = "Password cannot be empty!")
    @NotBlank(message = "Password cannot be blank!")
    String currentPassword;
    @NotEmpty(message = "Password cannot be empty!")
    @Size(min = 4, max = 12, message = "Password's length must be between 4 and 12")
    String newPassword;
    @NotEmpty(message = "Password cannot be empty!")
    @Size(min = 4, max = 12, message = "Password's length must be between 4 and 12")
    String repeatPassword;
}
