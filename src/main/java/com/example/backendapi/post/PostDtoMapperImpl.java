package com.example.backendapi.post;
import com.example.backendapi.comment.CommentRepository;
import com.example.backendapi.user.AppUserRepository;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Service;




@Service
@AllArgsConstructor
public class PostDtoMapperImpl implements PostDtoMapper {
    @NonNull private final AppUserRepository appUserRepository;
    @NonNull private final CommentRepository commentRepository;


    public PostDto map(Post post){
        PostDto postDto = new PostDto();
        postDto.setId(post.getId());
        postDto.setContent(post.getContent());
        postDto.setCreatedAt(post.getCreatedAt());
        postDto.setLikesNumber(post.getLikesNumber());
        postDto.setOwnerId(post.getOwner().getId());
        postDto.setOwnerName(post.getOwner().getUsername());
        postDto.setCommentsNumber(commentRepository
                .countCommentByPostId(post.getId()));
        return postDto;
    }

    public Post map(PostDto postDto){
        Post post = new Post();
        post.setId(postDto.getId());
        post.setContent(postDto.getContent());
        post.setCreatedAt(postDto.getCreatedAt());
        post.setLikesNumber(postDto.getLikesNumber());
        if(postDto.getOwnerId()!=null) {
            appUserRepository.findById(postDto.getOwnerId())
                    .ifPresent(post::setOwner);
        }
        return post;
    }










}
