package com.example.backendapi.comment;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query("SELECT c FROM Comment c WHERE c.post.id = ?1")
    List<Comment> findAllByPostId(Long postId, Pageable pageable);

    @Query("SELECT COUNT(c) FROM Comment c WHERE  c.post.id = ?1")
    long countCommentByPostId(Long postId);

}
