package com.curio.blog.service;

import com.curio.blog.dto.UserDto;
import com.curio.blog.dto.UserRegisterRequest;
import com.curio.blog.dto.UserUpdateRequest;
import com.curio.blog.exception.ResourceAlreadyExistsException;
import com.curio.blog.exception.ResourceNotFoundException;
import com.curio.blog.model.Role;
import com.curio.blog.model.User;
import com.curio.blog.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {
    private UserRepository userRepository;

    public UserDto updateUser(Long id, UserUpdateRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        user.setFullName(request.getFullName());
        user.setActive(request.isActive());

        User updatedUser = userRepository.save(user);
        return mapToDto(updatedUser);
    }

    public Page<UserDto> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable).map(this::mapToDto);
    }

    public UserDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        return mapToDto(user);
    }

    public void deleteUserById(Long id) {
        User user =  userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        userRepository.delete(user);
    }

    private UserDto mapToDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .active(user.isActive())
                .role(user.getRole())
                .build();
    }
}
