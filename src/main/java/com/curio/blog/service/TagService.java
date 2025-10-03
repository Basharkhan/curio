package com.curio.blog.service;

import com.curio.blog.dto.TagDto;
import com.curio.blog.dto.TagRequest;
import com.curio.blog.exception.ResourceAlreadyExistsException;
import com.curio.blog.exception.ResourceNotFoundException;
import com.curio.blog.model.Tag;
import com.curio.blog.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TagService {
    private  final TagRepository tagRepository;

    @Transactional
    public TagDto createTag(TagRequest request) {
        if (tagRepository.findByName(request.getName()).isPresent()) {
            throw new ResourceAlreadyExistsException("Tag already exists: " + request.getName());
        }

        Tag tag = Tag.builder()
                .name(request.getName())
                .build();

        return mapToDto(tagRepository.save(tag));
    }

    @Transactional
    public TagDto updateTag(Long id, TagRequest request) {
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tag not found with id: " + id));

        tag.setName(request.getName());
        return mapToDto(tagRepository.save(tag));
    }

    @Transactional
    public void deleteTagById(Long id) {
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tag not found with id: " + id));
        tagRepository.delete(tag);
    }

    @Transactional(readOnly = true)
    public Page<TagDto> getAllTags(String search, Pageable pageable) {
        Page<Tag> tags;

        if (search != null && !search.isEmpty()) {
            tags = tagRepository.findByNameContainingIgnoreCase(search, pageable);
        } else {
            tags = tagRepository.findAll(pageable);
        }

        return tags.map(this::mapToDto);
    }

    @Transactional(readOnly = true)
    public TagDto getTagById(Long id) {
        return tagRepository.findById(id)
                .map(this::mapToDto)
                .orElseThrow(() -> new ResourceNotFoundException("Tag not found with id: " + id));
    }

    private TagDto mapToDto(Tag tag) {
        return TagDto.builder()
                .id(tag.getId())
                .name(tag.getName())
                .build();
    }
}
