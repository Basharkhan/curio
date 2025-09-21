package com.curio.blog.dto;

import com.curio.blog.model.Role;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDto {
    private Long id;
    private String fullName;
    private String email;
    private boolean active;
    private Role role;
}

