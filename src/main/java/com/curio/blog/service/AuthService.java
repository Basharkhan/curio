package com.curio.blog.service;

import com.curio.blog.dto.*;
import com.curio.blog.exception.InvalidCredentialsException;
import com.curio.blog.exception.ResourceAlreadyExistsException;
import com.curio.blog.exception.ResourceNotFoundException;
import com.curio.blog.model.Role;
import com.curio.blog.model.User;
import com.curio.blog.repository.UserRepository;
import com.curio.blog.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.expression.spel.ast.OpAnd;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public AuthResponse registerAdmin(UserRegisterRequest request) {
        return register(request, Role.ADMIN);
    }

    @Transactional
    public AuthResponse registerUser(UserRegisterRequest request) {
        return register(request, Role.AUTHOR);
    }

    private AuthResponse register(UserRegisterRequest request, Role role) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ResourceAlreadyExistsException("User already exists with email: " + request.getEmail());
        }

        User user = User.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(role)
                .active(true)
                .build();

        User savedUser = userRepository.save(user);

        String token = jwtService.generateToken(savedUser);

        UserDetailsDto userDetailsDto = UserDetailsDto.builder()
                .fullName(savedUser.getFullName())
                .email(savedUser.getEmail())
                .role(savedUser.getRole())
                .build();

        return AuthResponse.builder()
                .token(token)
                .userDetailsDto(userDetailsDto)
                .build();
    }

    public AuthResponse login(LoginRequest loginRequest) {
        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + loginRequest.getEmail()));

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Invalid credentials");
        }

        String token = jwtService.generateToken(user);

        UserDetailsDto userDetailsDto = UserDetailsDto.builder()
                .email(user.getEmail())
                .fullName(user.getFullName())
                .role(user.getRole())
                .build();

        return AuthResponse.builder()
                .token(token)
                .userDetailsDto(userDetailsDto)
                .build();
    }

    @Transactional(readOnly = true)
    public UserDetailsDto getCurrentUser(Authentication authentication) {
        String email = authentication.getName();
        Optional<User> user = userRepository.findByEmail(email);

        if (user.isEmpty()) {
            throw new ResourceNotFoundException("User not found with email: " + email);
        }

        return UserDetailsDto.builder()
                .email(user.get().getEmail())
                .fullName(user.get().getFullName())
                .role(user.get().getRole())
                .build();
    }
}
