package com.curio.blog.dto;


import com.curio.blog.model.Role;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDetailsDto {
    private String email;
    private String fullName;
    private Role role;
}
