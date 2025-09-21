package com.curio.blog.controller;

import com.curio.blog.dto.*;
import com.curio.blog.service.PostService;
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
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @PostMapping
    public ResponseEntity<ApiResponse<PostDto>> registerUser(@Valid @RequestBody PostCreateRequest request) {
        PostDto post = postService.createPost(request);

        ApiResponse<PostDto> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                "Post created successfully",
                post,
                LocalDateTime.now()
        );

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<PostDto>> updatePost(@PathVariable Long id,
                                                           @Valid @RequestBody PostUpdateRequest request) {
        PostDto post = postService.updatePost(id, request);

        ApiResponse<PostDto> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                "Post updated successfully",
                post,
                LocalDateTime.now()
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PostDto>> getPostById(@PathVariable Long id) {
        PostDto post = postService.getPostById(id);

        ApiResponse<PostDto> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                "Post retrieved successfully",
                post,
                LocalDateTime.now()
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<PostDto>>> getAllPosts(@PageableDefault(size = 10, page = 0) Pageable pageable) {
        Page<PostDto> posts = postService.getAllPosts(pageable);

        ApiResponse<Page<PostDto>> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                "Post retrieved successfully",
                posts,
                LocalDateTime.now()
        );

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deletePost(@PathVariable Long id) {
        postService.deletePost(id);

        ApiResponse<Void> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                "Post deleted successfully",
                null,
                LocalDateTime.now()
        );

        return ResponseEntity.ok(response);
    }
}
