package com.example.backendapi.comment;

import com.example.backendapi.post.PostRepository;
import com.example.backendapi.user.AppUserRepository;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CommentDtoMapperImpl implements CommentDtoMapper{
    @NonNull private final AppUserRepository appUserRepository;

    @NonNull private final PostRepository postRepository;

    public CommentDto map(Comment comment){
        CommentDto commentDto = new CommentDto();
        commentDto.setPostId(comment.getPost().getId());
        commentDto.setId(comment.getId());
        commentDto.setContent(comment.getContent());
        commentDto.setOwnerId(comment.getOwner().getId());
        commentDto.setOwnerName(comment.getOwner().getUsername());
        commentDto.setCreatedAt(comment.getCreatedAt());
        commentDto.setLikesNumber(comment.getLikesNumber());
        return commentDto;
    }

    public Comment map(CommentDto commentDto){
        Comment comment = new Comment();
        if(commentDto.getPostId() != null) {
            postRepository.findById(commentDto.getPostId())
                    .ifPresent(comment::setPost);
        }
        comment.setId(commentDto.getId());
        comment.setContent(commentDto.getContent());
        if(commentDto.getOwnerId() != null) {
            appUserRepository.findById(commentDto.getOwnerId())
                    .ifPresent(comment::setOwner);
        }
        comment.setCreatedAt(commentDto.getCreatedAt());
        comment.setLikesNumber(commentDto.getLikesNumber());
        return comment;
    }
}
