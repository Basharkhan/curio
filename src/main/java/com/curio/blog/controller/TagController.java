package com.curio.blog.controller;

import com.curio.blog.dto.*;
import com.curio.blog.service.TagService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/tags")
@RequiredArgsConstructor
public class TagController {
    private final TagService tagService;

    @PostMapping
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

    @PutMapping("/{id}")
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

    @GetMapping
    public ResponseEntity<ApiResponse<List<TagDto>>> getAllTags() {
        List<TagDto> tags = tagService.getAllTags();

        ApiResponse<List<TagDto>> response = new ApiResponse<>(
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

    @DeleteMapping("/{id}")
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
