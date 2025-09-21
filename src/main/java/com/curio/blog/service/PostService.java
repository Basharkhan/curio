package com.curio.blog.service;

import com.curio.blog.dto.PostCreateRequest;
import com.curio.blog.dto.PostDto;
import com.curio.blog.dto.PostUpdateRequest;
import com.curio.blog.exception.ResourceNotFoundException;
import com.curio.blog.model.*;
import com.curio.blog.repository.CategoryRepository;
import com.curio.blog.repository.PostRepository;
import com.curio.blog.repository.TagRepository;
import com.curio.blog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final TagRepository tagRepository;
    private final CategoryRepository categoryRepository;

    public PostDto createPost(PostCreateRequest request) {
        User author = userRepository.findById(request.getAuthorId())
                .orElseThrow(() -> new ResourceNotFoundException("Author not found with id: " + request.getAuthorId()));
        Category category = null;
        if (request.getCategoryId() != null) {
            category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + request.getCategoryId()));
        }

        List<Tag> tags = new ArrayList<>();
        if (request.getTagIds() != null && !request.getTagIds().isEmpty()) {
            tags = tagRepository.findAllById(request.getTagIds());
        }

        Post post = Post.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .author(author)
                .status(PostStatus.DRAFT)
                .category(category)
                .tags(tags)
                .build();

        Post createdPost = postRepository.save(post);
        return mapToDto(createdPost);
    }

    public PostDto updatePost(Long id, PostUpdateRequest request) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + id));

        List<Tag> tags = new ArrayList<>();
        if (request.getTagIds() != null && !request.getTagIds().isEmpty()) {
            tags = tagRepository.findAllById(request.getTagIds());
        }

        post.setTitle(request.getTitle());
        post.setContent(request.getContent());

        if (request.getCategoryId() != null) {
            Category category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + request.getCategoryId()));
            post.setCategory(category);
        }

        if (request.getTagIds() != null) {
            post.setTags(tags);
        }

        if (request.getStatus() != null) {
            post.setStatus(request.getStatus());
        }

        return mapToDto(postRepository.save(post));
    }

    public PostDto getPostById(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + id));
        return mapToDto(post);
    }

    public Page<PostDto> getAllPosts(Pageable pageable) {
        return postRepository.findAll(pageable)
                .map(this::mapToDto);
    }

    public void deletePost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found"));
        postRepository.delete(post);
    }

    private PostDto mapToDto(Post post) {
        return PostDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .authorName(post.getAuthor().getFullName())
                .categoryName(post.getCategory() != null ? post.getCategory().getName() : null)
                .tags(post.getTags() != null ? post.getTags().stream().map(Tag::getName).toList() : List.of())
                .status(post.getStatus())
                .views(post.getViews())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .build();
    }
}
