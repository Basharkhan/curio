package com.curio.blog.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserRegisterRequest {
    @NotBlank(message = "Full name is required")
    String fullName;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    String email;

    @Size(min = 6, max = 20, message = "Password should be between 6 to 20 characters")
    @NotBlank(message = "Password is required")
    String password;
}
