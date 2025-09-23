package com.curio.blog.controller;

import com.curio.blog.dto.*;
import com.curio.blog.service.PostService;
import com.curio.blog.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final PostService postService;

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UserDto>> updateUser(@PathVariable Long id,
                                                           @Valid @RequestBody UserUpdateRequest request) {
        UserDto user = userService.updateUser(id, request);

        ApiResponse<UserDto> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                "User updated successfully",
                user,
                LocalDateTime.now()
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<UserDto>>> getAllUsers(@PageableDefault(size = 10, page = 0) Pageable pageable) {
        Page<UserDto> users = userService.getAllUsers(pageable);

        ApiResponse<Page<UserDto>> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                "Users retrieved successfully",
                users,
                LocalDateTime.now()
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserDto>> getUserById(@PathVariable Long id) {
        UserDto user = userService.getUserById(id);

        ApiResponse<UserDto> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                "User retrieved successfully",
                user,
                LocalDateTime.now()
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/posts")
    public ResponseEntity<ApiResponse<Page<PostDto>>> getPostsByAuthor(@PathVariable Long id,
                                                                       @PageableDefault(size = 10, page = 0) Pageable pageable) {
        Page<PostDto> posts = postService.getPostsByAuthor(id, pageable);

        ApiResponse<Page<PostDto>> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                "Posts retrieved successfully",
                posts,
                LocalDateTime.now()
        );

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUserById(@PathVariable Long id) {
        userService.deleteUserById(id);

        ApiResponse<Void> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                "User deleted successfully",
                null,
                LocalDateTime.now()
        );

        return ResponseEntity.ok(response);
    }
}
