package com.curio.blog.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserUpdateRequest {
    @NotBlank(message = "Full name is required")
    String fullName;
    private boolean active;
}
