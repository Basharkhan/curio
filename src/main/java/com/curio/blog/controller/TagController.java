package com.curio.blog.controller;

import com.curio.blog.dto.*;
import com.curio.blog.service.PostService;
import com.curio.blog.service.TagService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/tags")
@RequiredArgsConstructor
public class TagController {
    private final TagService tagService;
    private final PostService postService;

    // ============================
    // public endpoints
    // ============================
    @GetMapping
    public ResponseEntity<ApiResponse<Page<TagDto>>> getAllTags(
            @RequestParam(value = "search", required = false) String search,
            @PageableDefault(size = 10, page = 0) Pageable pageable) {
        Page<TagDto> tags = tagService.getAllTags(search, pageable);

        ApiResponse<Page<TagDto>> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                "Tags retrieved successfully",
                tags,
                LocalDateTime.now()
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TagDto>> getTagById(@PathVariable Long id) {
        TagDto tag = tagService.getTagById(id);

        ApiResponse<TagDto> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                "Tag retrieved successfully",
                tag,
                LocalDateTime.now()
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/posts")
    public ResponseEntity<ApiResponse<Page<PostDto>>> getPostsByTag(@PathVariable Long id,
                                                                    @PageableDefault(size = 10, page = 0) Pageable pageable) {
        Page<PostDto> posts = postService.getPostsByTag(id, pageable);

        ApiResponse<Page<PostDto>> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                "Posts by tag retrieved successfully",
                posts,
                LocalDateTime.now()
        );

        return ResponseEntity.ok(response);
    }

    // ============================
    // admin endpoints
    // ============================
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<TagDto>> createTag(@Valid @RequestBody TagRequest request) {
        TagDto user = tagService.createTag(request);

        ApiResponse<TagDto> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                "Tag created successfully",
                user,
                LocalDateTime.now()
        );

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<TagDto>> updateTag(@PathVariable Long id,
                                                         @Valid @RequestBody TagRequest request) {
        TagDto user = tagService.updateTag(id, request);

        ApiResponse<TagDto> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                "Tag updated successfully",
                user,
                LocalDateTime.now()
        );

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteTagById(@PathVariable Long id) {
        tagService.deleteTagById(id);

        ApiResponse<Void> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                "Tag deleted successfully",
                null,
                LocalDateTime.now()
        );

        return ResponseEntity.ok(response);
    }
}
