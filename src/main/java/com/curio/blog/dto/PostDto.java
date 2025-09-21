package com.curio.blog.dto;

import com.curio.blog.model.PostStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class PostDto {
    private Long id;
    private String title;
    private String content;
    private String authorName;
    private String categoryName;
    private List<String> tags;
    private PostStatus status;
    private int views;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
