package com.curio.blog.repository;

import com.curio.blog.model.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TagRepository extends JpaRepository<Tag,Long> {
    Optional<Tag> findByName(String name);
    boolean existsById(Long id);
    Page<Tag> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
