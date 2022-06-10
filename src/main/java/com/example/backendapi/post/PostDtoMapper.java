package com.example.backendapi.post;

public interface PostDtoMapper {

    PostDto map(Post post);

    Post map(PostDto postDto);
}
