package com.example.test.unit;

import com.example.backendapi.BackendApiApplication;
import com.example.backendapi.comment.Comment;
import com.example.backendapi.comment.CommentDto;
import com.example.backendapi.comment.CommentDtoMapper;
import com.example.backendapi.post.Post;
import com.example.backendapi.post.PostRepository;
import com.example.backendapi.user.AppUser;
import com.example.backendapi.user.AppUserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Profile;

import java.time.LocalDateTime;
import java.util.Optional;

@SpringBootTest(classes = BackendApiApplication.class)
@Profile("test")
public class CommentDtoMapperTests {

    private final AppUser appUser = new AppUser();
    private final Post post = new Post();

    @MockBean
    private AppUserRepository appUserRepository;

    @MockBean
    private PostRepository postRepository;

    @Autowired
    private CommentDtoMapper mapper;


    @BeforeEach
    public void init(){
        MockitoAnnotations.openMocks(this);
        appUser.setId(25L);
        appUser.setUsername("Jack");
        post.setId(11L);
    }

    @Test
    public void given_comment_should_return_commentDto(){
        //given
        Comment comment = new Comment();
        comment.setPost(post);
        comment.setId(27L);
        comment.setContent("Hello tests");
        comment.setOwner(appUser);
        comment.setCreatedAt(LocalDateTime.now());
        comment.setLikesNumber(6);

        //when
        CommentDto commentDto = mapper.map(comment);

        //then
        Assertions.assertEquals(comment.getPost().getId(), commentDto.getPostId());
        Assertions.assertEquals(comment.getId(),commentDto.getId());
        Assertions.assertEquals(comment.getContent(), commentDto.getContent());
        Assertions.assertEquals(comment.getOwner().getId(), commentDto.getOwnerId());
        Assertions.assertEquals(comment.getOwner().getUsername(), commentDto.getOwnerName());
        Assertions.assertEquals(comment.getCreatedAt(), commentDto.getCreatedAt());
        Assertions.assertEquals(comment.getLikesNumber(), commentDto.getLikesNumber());


    }


    @Test
    public void given_commentDto_should_return_comment(){
        //given
        Mockito.when(appUserRepository.findById(25L)).thenReturn(Optional.of(appUser));
        Mockito.when(postRepository.findById(11L)).thenReturn(Optional.of(post));

        CommentDto commentDto = new CommentDto();
        commentDto.setPostId(11L);
        commentDto.setId(27L);
        commentDto.setContent("Hello tests");
        commentDto.setOwnerId(25L);
        commentDto.setOwnerName("Jack");
        commentDto.setCreatedAt(LocalDateTime.now());
        commentDto.setLikesNumber(6);

        //when
        Comment comment = mapper.map(commentDto);

        //then
        Assertions.assertEquals(commentDto.getPostId(), comment.getPost().getId());
        Assertions.assertEquals(commentDto.getId(),comment.getId());
        Assertions.assertEquals(commentDto.getContent(), comment.getContent());
        Assertions.assertEquals(commentDto.getOwnerId(), comment.getOwner().getId());
        Assertions.assertEquals(commentDto.getOwnerName(), comment.getOwner().getUsername());
        Assertions.assertEquals(commentDto.getCreatedAt(), comment.getCreatedAt());
        Assertions.assertEquals(commentDto.getLikesNumber(), comment.getLikesNumber());


    }

}
