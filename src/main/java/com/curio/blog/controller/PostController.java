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
import java.util.List;

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

    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<PostDto>> updatePostStatus(@PathVariable Long id,
                                                                 @Valid @RequestBody UpdateStatusRequest request) {
        PostDto post = postService.updatePostStatus(id, request);

        ApiResponse<PostDto> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                "Post status updated successfully",
                post,
                LocalDateTime.now()
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<PostDto>>> searchPosts(@RequestParam(defaultValue = "") String query,
                                                                  @PageableDefault(size = 10, page = 0) Pageable pageable) {
        Page<PostDto> posts = postService.searchPosts(query, pageable);

        ApiResponse<Page<PostDto>> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                "Post with search query retrieved successfully",
                posts,
                LocalDateTime.now()
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/featured")
    public ResponseEntity<ApiResponse<List<PostDto>>> getFeaturedPosts() {
        List<PostDto> posts = postService.getFeaturedPosts();

        ApiResponse<List<PostDto>> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                "Featured posts retrieved successfully",
                posts,
                LocalDateTime.now()
        );

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/toggle-featured")
    public ResponseEntity<ApiResponse<PostDto>> toggleFeatured(@PathVariable Long id) {
        PostDto post = postService.toggleFeatured(id);
        String message = post.isFeatured() ? "Post marked as featured" : "Post unmarked as featured";

        ApiResponse<PostDto> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                message,
                post,
                LocalDateTime.now()
        );

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/post-views")
    public ResponseEntity<ApiResponse<PostDto>> getPostAndIncrementViews(@PathVariable Long id) {
        PostDto post = postService.getPostAndIncrementViews(id);
        String message = "Post views incremented";

        ApiResponse<PostDto> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                message,
                post,
                LocalDateTime.now()
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/recent")
    public ResponseEntity<ApiResponse<List<PostDto>>> getRecentPosts(@RequestParam(defaultValue = "5") int limit) {
        List<PostDto> posts = postService.getRecentPosts(limit);
        String message = "Recent post retrieved successfully";

        ApiResponse<List<PostDto>> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                message,
                posts,
                LocalDateTime.now()
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/related")
    public ResponseEntity<ApiResponse<List<PostDto>>> getRelatedPosts(@PathVariable Long id,
                                                                      @RequestParam(defaultValue = "10") int limit) {
        List<PostDto> posts = postService.getRelatedPosts(id, limit);
        String message = "Related post retrieved successfully";

        ApiResponse<List<PostDto>> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                message,
                posts,
                LocalDateTime.now()
        );

        return ResponseEntity.ok(response);
    }
}
