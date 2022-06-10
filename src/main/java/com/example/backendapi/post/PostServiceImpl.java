package com.example.backendapi.post;
import com.example.backendapi.exceptions.PostNotFoundException;
import com.example.backendapi.user.AuthUtils;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Service
@AllArgsConstructor
public class PostServiceImpl implements PostService{
    private final PostRepository postRepository;
    private final PostDtoMapper postDtoMapper;

    private final int PAGE_SIZE = 5;


    @Override
    public PostDto add(CreatePostRequest request) {
        Post postToSave = new Post();
        postToSave.setContent(request.getContent());
        postToSave.setOwner(AuthUtils.getCurrentPrincipal());
        postToSave.setCreatedAt(LocalDateTime.now());
        Post savedPost = postRepository.save(postToSave);
        return postDtoMapper.map(savedPost);
    }
    public void delete(Long id){
        postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException(id));
        postRepository.deleteById(id);
    }

    @Override
    @Transactional
    public Optional<PostDto> editContent(Long postId, PostNewContentRequest request) {
        return postRepository.findById(postId)
                .map(target -> setContent(request.getNewContent(), target))
                .map(postDtoMapper::map);
    }

    @Override
    @Transactional
    public Optional<PostDto> likePost(Long postId) {
        return postRepository.findById(postId)
                .map(this::addLike)
                .map(postDtoMapper::map);
    }
    @Override
    public Optional<PostDto> findById(Long id){
        return postRepository.findById(id).map(postDtoMapper::map);
    }

    @Override
    public List<PostDto> findAllMyPosts(Integer page) {
        int pageNumber = page != null && page >= 0 ? page : 0;
        Long userId = AuthUtils.getCurrentPrincipal().getId();
        return postRepository.findAllByOwnerId(userId, PageRequest.of(pageNumber,PAGE_SIZE))
                .stream()
                .map(postDtoMapper::map)
                .toList();
    }


    private Post setContent(String content, Post target){
        target.setContent(content);
        return target;
    }
    private Post addLike(Post target){
        target.setLikesNumber(target.getLikesNumber() + 1);
        return target;
    }




}

