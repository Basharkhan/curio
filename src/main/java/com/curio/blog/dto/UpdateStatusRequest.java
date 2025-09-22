package com.curio.blog.dto;

import com.curio.blog.model.PostStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateStatusRequest {
    @NotNull(message = "Status is required")
    private PostStatus status;
}
