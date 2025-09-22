package com.curio.blog.repository;

import com.curio.blog.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    Page<Post> findByAuthorId(Long authorId, Pageable pageable);
    Page<Post> findByCategoryId(Long categoryId, Pageable pageable);
    Page<Post> findByTags_Id(Long tagId, Pageable pageable);
    Page<Post> findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(String titleQuery, String contentQuery, Pageable pageable);
    List<Post> findByFeaturedTrue();
    List<Post> findByCategoryIdAndIdNot(Long categoryId, Long postId, Pageable pageable);
    List<Post> findByTags_IdInAndIdNot(List<Long> tagIds, Long postId, Pageable pageable);
}
