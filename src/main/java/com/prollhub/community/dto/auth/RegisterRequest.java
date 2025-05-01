package com.prollhub.community.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequest {

    @NotBlank(message = "USERNAME_REQUIRED")
    @Size(min = 3, max = 50, message = "USERNAME_LENGTH_INVALID")
    @Pattern(regexp = "^[a-zA-Z0-9_-]+$", message = "USERNAME_FORMAT_INVALID")
    private String username;

    @NotBlank(message = "EMAIL_REQUIRED")
    @Email(message = "EMAIL_LENGTH_INVALID")
    @Size(max = 100, message = "EMAIL_FORMAT_INVALID")
    private String email;

    @NotBlank(message = "TOKEN_REQUIRED")
    @Size(min=36, max=36, message = "TOKEN_FORMAT_INVALID")
    private String token;

    @NotBlank(message = "PASSWORD_REQUIRED")
    @Size(min = 8, max = 100, message = "PASSWORD_LENGTH_INVALID")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?~])[A-Za-z0-9!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?~]+$",
            message = "PASSWORD_FORMAT_INVALID")
    private String password;

}
