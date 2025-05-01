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
    private String password;
}
