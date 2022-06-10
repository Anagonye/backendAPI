package com.example.backendapi.post;




import java.util.List;
import java.util.Optional;
public interface PostService {

    PostDto add(CreatePostRequest request);

    void delete(Long id);

    Optional<PostDto> editContent(Long postId, PostNewContentRequest request);

    Optional<PostDto> likePost(Long postId);

    Optional<PostDto> findById(Long postId);

    List<PostDto> findAllMyPosts(Integer pageNumber);

}
