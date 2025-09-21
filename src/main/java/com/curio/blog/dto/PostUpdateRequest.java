package com.curio.blog.dto;

import com.curio.blog.model.PostStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
public class PostUpdateRequest {
    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Content is required")
    private String content;
    private PostStatus status;
    private Long categoryId;
    private List<Long> tagIds;
}
