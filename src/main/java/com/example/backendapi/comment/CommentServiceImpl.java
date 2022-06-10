package com.example.backendapi.comment;

import com.example.backendapi.exceptions.CommentNotFoundException;
import com.example.backendapi.exceptions.PostNotFoundException;
import com.example.backendapi.post.PostService;
import com.example.backendapi.user.AuthUtils;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
@Service
@AllArgsConstructor
public class CommentServiceImpl implements CommentService{

    @NonNull private final CommentDtoMapper dtoMapper;
    @NonNull private final CommentDtoRequestMapper requestMapper;
    @NonNull private final CommentRepository commentRepository;
    @NonNull private final PostService postService;
    private final int PAGE_SIZE = 5;



    @Override
    public CommentDto add(CreateCommentRequest request) {
        postService.findById(request.getPostId())
                .orElseThrow(() -> new PostNotFoundException(request.getPostId()));
        Comment commentToSave = dtoMapper
                .map(requestMapper.map(request));
        setBasicInfo(commentToSave);
        Comment savedComment = commentRepository.save(commentToSave);
        return dtoMapper.map(savedComment);
    }

    @Override
    public void delete(Long commentId) {
        commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException(commentId));
        commentRepository.deleteById(commentId);
    }

    @Override
    @Transactional
    public Optional<CommentDto> likeComment(Long commentId) {
        return commentRepository.findById(commentId)
                .map(this::addLike)
                .map(dtoMapper::map);
    }

    @Override
    public Optional<CommentDto> editComment(Long commentId, CommentNewContentRequest request) {
        return commentRepository.findById(commentId)
                .map(comment -> setContent(request.getNewContent(), comment))
                .map(dtoMapper::map);
    }

    @Override
    public Optional<CommentDto> findById(Long id) {
        return commentRepository.findById(id).map(dtoMapper::map);
    }

    @Override
    public List<CommentDto> findAllByPostId(Long postId, Integer page) {
        postService.findById(postId).orElseThrow(() -> new PostNotFoundException(postId));
        int pageNumber = page != null && page >= 0 ? page : 0;
        return commentRepository.findAllByPostId(postId, PageRequest.of(pageNumber,PAGE_SIZE))
                .stream()
                .map(dtoMapper::map)
                .toList();
    }

    private void setBasicInfo(Comment target){
        target.setOwner(AuthUtils.getCurrentPrincipal());
        target.setCreatedAt(LocalDateTime.now());
    }

    private Comment setContent(String content, Comment target){
        target.setContent(content);
        return target;
    }
    private Comment addLike(Comment target){
        target.setLikesNumber(target.getLikesNumber() + 1);
        return target;
    }
}
