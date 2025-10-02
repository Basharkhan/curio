package com.curio.blog.controller;

import com.curio.blog.dto.ApiResponse;
import com.curio.blog.dto.AuthResponse;
import com.curio.blog.dto.LoginRequest;
import com.curio.blog.dto.UserRegisterRequest;
import com.curio.blog.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final Environment env;

    @PostMapping("/register/admin")
    public ResponseEntity<ApiResponse<AuthResponse>> registerAdmin(@RequestHeader("X-SETUP-KEY") String setupKey,
                                                                   @Valid @RequestBody UserRegisterRequest request) {
        String expectedKey = env.getProperty("app.setup.key");

        if (expectedKey == null || !expectedKey.equals(setupKey)) {
            throw new AccessDeniedException("Access denied");
        }

        AuthResponse authResponse = authService.registerAdmin(request);

        ApiResponse<AuthResponse> apiResponse = new ApiResponse<>(
                HttpStatus.OK.value(),
                "Admin registered successfully",
                authResponse,
                LocalDateTime.now()
        );

        return ResponseEntity.ok(apiResponse);
    }

    @PostMapping("/register/user")
    public ResponseEntity<ApiResponse<AuthResponse>> registerUser(@Valid @RequestBody UserRegisterRequest request) {
        AuthResponse authResponse = authService.registerUser(request);

        ApiResponse<AuthResponse> apiResponse = new ApiResponse<>(
                HttpStatus.OK.value(),
                "Author registered successfully",
                authResponse,
                LocalDateTime.now()
        );

        return ResponseEntity.ok(apiResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@RequestBody LoginRequest request) {
        AuthResponse authResponse = authService.login(request);

        ApiResponse<AuthResponse> apiResponse = new ApiResponse<>(
                HttpStatus.OK.value(),
                "Login successful",
                authResponse,
                LocalDateTime.now()
        );

        return ResponseEntity.ok(apiResponse);
    }
}
