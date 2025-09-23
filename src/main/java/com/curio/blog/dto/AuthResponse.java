package com.curio.blog.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class AuthResponse {
    String token;
    UserDetailsDto userDetailsDto;
}
