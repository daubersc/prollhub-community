package com.prollhub.community.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LoginRequest {

    @NotBlank(message = "EMAIL_REQUIRED")
    @Email(message = "EMAIL_LENGTH_INVALID")
    @Size(max = 100, message = "EMAIL_FORMAT_INVALID")
    private String email;

    @Size(min = 8, max = 100, message = "PASSWORD_LENGTH_INVALID")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?~])[A-Za-z0-9!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?~]+$",
            message = "PASSWORD_FORMAT_INVALID")
    private String password;
}
