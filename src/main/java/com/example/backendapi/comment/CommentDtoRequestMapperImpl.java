package com.example.backendapi.comment;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CommentDtoRequestMapperImpl implements CommentDtoRequestMapper {

    @Override
    public CommentDto map(CreateCommentRequest request) {
        CommentDto commentDto = new CommentDto();
        commentDto.setPostId(request.getPostId());
        commentDto.setContent(request.getContent());
        return commentDto;
    }
}
