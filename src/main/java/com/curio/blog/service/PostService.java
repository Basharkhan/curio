package com.curio.blog.service;

import com.curio.blog.dto.PostCreateRequest;
import com.curio.blog.dto.PostDto;
import com.curio.blog.dto.PostUpdateRequest;
import com.curio.blog.dto.UpdateStatusRequest;
import com.curio.blog.exception.ResourceNotFoundException;
import com.curio.blog.model.*;
import com.curio.blog.repository.CategoryRepository;
import com.curio.blog.repository.PostRepository;
import com.curio.blog.repository.TagRepository;
import com.curio.blog.repository.UserRepository;
import com.curio.blog.utils.AuthUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final TagRepository tagRepository;
    private final CategoryRepository categoryRepository;
    private final AuthUtils authUtils;

    @Transactional
    public PostDto createPost(PostCreateRequest request) {
        User author = authUtils.getAuthenticatedUser();

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

    @Transactional
    public PostDto updatePost(Long id, PostUpdateRequest request) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + id));

        // find all the tags
        List<Tag> tags = new ArrayList<>();
        if (request.getTagIds() != null && !request.getTagIds().isEmpty()) {
            tags = tagRepository.findAllById(request.getTagIds());
        }

        post.setTitle(request.getTitle());
        post.setContent(request.getContent());

        // find the category
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

    @Transactional(readOnly = true)
    public PostDto getPostById(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + id));
        return mapToDto(post);
    }

    @Transactional(readOnly = true)
    public Page<PostDto> getAllPosts(Pageable pageable) {
        return postRepository.findAll(pageable)
                .map(this::mapToDto);
    }

    @Transactional
    public void deletePost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found"));
        postRepository.delete(post);
    }

    @Transactional
    public PostDto updatePostStatus(Long postId, UpdateStatusRequest request) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + postId));

        post.setStatus(request.getStatus());
        Post updatedPost = postRepository.save(post);

        return mapToDto(updatedPost);
    }

    @Transactional(readOnly = true)
    public Page<PostDto> getPostsByAuthor(Long authorId, Pageable pageable) {
        if (!userRepository.existsById(authorId)) {
            throw new ResourceNotFoundException("Author not found with id: " + authorId);
        }

        Page<Post> posts = postRepository.findByAuthorId(authorId, pageable);
        return posts.map(this::mapToDto);
    }

    @Transactional(readOnly = true)
    public Page<PostDto> getPostsByCategory(Long categoryId, Pageable pageable) {
        if (!categoryRepository.existsById(categoryId)) {
            throw new ResourceNotFoundException("Category not found with id: " + categoryId);
        }

        Page<Post> posts = postRepository.findByCategoryId(categoryId, pageable);
        return posts.map(this::mapToDto);
    }

    @Transactional(readOnly = true)
    public Page<PostDto> getPostsByTag(Long tagId, Pageable pageable) {
        if (!tagRepository.existsById(tagId)) {
            throw new ResourceNotFoundException("Tag not found with id: " + tagId);
        }

        Page<Post> posts = postRepository.findByTags_Id(tagId, pageable);
        return posts.map(this::mapToDto);
    }

    @Transactional(readOnly = true)
    public Page<PostDto> searchPosts(String query, Pageable pageable) {
        Page<Post> posts;

        if (query == null || query.isBlank()) {
            posts = Page.empty(pageable);
        } else {
            posts = postRepository.findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(query, query, pageable);
        }
        return posts.map(this::mapToDto);
    }

    @Transactional
    public PostDto getPostAndIncrementViews(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + postId));

        post.setViews(post.getViews() + 1);
        Post updatedPost = postRepository.save(post);

        return mapToDto(updatedPost);
    }

    @Transactional
    public PostDto toggleFeatured(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + id));
        post.setFeatured(!post.isFeatured());
        return mapToDto(postRepository.save(post));
    }

    @Transactional(readOnly = true)
    public List<PostDto> getFeaturedPosts() {
        List<Post> posts = postRepository.findByFeaturedTrue();
        return posts.stream().map(this::mapToDto).toList();
    }

    @Transactional(readOnly = true)
    public List<PostDto> getRecentPosts(int limit) {
        Pageable pageable = PageRequest.of(0, limit, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Post> posts = postRepository.findAll(pageable);
        return posts.stream().map(this::mapToDto).toList();
    }

    @Transactional(readOnly = true)
    public List<PostDto> getRelatedPosts(Long postId, int limit) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + postId));

        // Find related posts by category first
        Pageable pageable = PageRequest.of(0, limit, Sort.by(Sort.Direction.DESC, "createdAt"));
        List<Post> related = new ArrayList<>();
        if (post.getCategory() != null) {
            related = postRepository.findByCategoryIdAndIdNot(post.getCategory().getId(), post.getId(), pageable);
        }

        // If not enough, fetch by tags
        if (related.size() < limit && post.getTags() != null && !post.getTags().isEmpty()) {
            List<Long> tagIds = post.getTags().stream().map(Tag::getId).toList();
            List<Post> tagRelated = postRepository.findByTags_IdInAndIdNot(tagIds, post.getId(), pageable);
            related.addAll(tagRelated);
        }

        return related.stream().map(this::mapToDto).distinct().limit(limit).toList();
    }

    private PostDto mapToDto(Post post) {
        return PostDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .authorName(post.getAuthor().getFullName())
                .categoryId(post.getCategory() != null ? post.getCategory().getId() : null)
                .categoryName(post.getCategory() != null ? post.getCategory().getName() : null)
                .tags(post.getTags() != null ? post.getTags().stream().map(Tag::getName).toList() : List.of())
                .status(post.getStatus())
                .views(post.getViews())
                .featured(post.isFeatured())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .build();
    }
}
