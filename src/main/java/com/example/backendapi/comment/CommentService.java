package com.example.backendapi.comment;


import java.util.List;
import java.util.Optional;

public interface CommentService {

    CommentDto add(CreateCommentRequest request);

    void delete(Long commentId);

    Optional<CommentDto> likeComment(Long commentId);

    Optional<CommentDto> editComment(Long commentId, CommentNewContentRequest request);

    Optional<CommentDto> findById(Long id);

    List<CommentDto> findAllByPostId(Long postId, Integer pageNumber);

}
