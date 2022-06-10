package com.example.backendapi.comment;

public interface CommentDtoMapper {

    CommentDto map(Comment comment);

    Comment map(CommentDto commentDto);
}
