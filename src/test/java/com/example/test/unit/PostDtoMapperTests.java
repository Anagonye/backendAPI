package com.example.test.unit;

import com.example.backendapi.BackendApiApplication;
import com.example.backendapi.post.Post;
import com.example.backendapi.post.PostDto;
import com.example.backendapi.post.PostDtoMapper;
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
public class PostDtoMapperTests {


    private final AppUser appUser = new AppUser();

    @Autowired
    private PostDtoMapper mapper;

    @MockBean
    AppUserRepository appUserRepository;

    @BeforeEach
    public void init(){
        MockitoAnnotations.openMocks(this);
        appUser.setId(25L);
    }
    @Test
    public void given_post_should_return_postDto(){
        //given
        Post post = new Post();
        post.setId(11L);
        post.setContent("Hello tests");
        post.setCreatedAt(LocalDateTime.now());
        post.setOwner(appUser);
        post.setLikesNumber(15);
        post.setComments(null);

        //when
        PostDto postDto = mapper.map(post);

        //then
        Assertions.assertEquals(post.getId(), postDto.getId());
        Assertions.assertEquals(post.getContent(), postDto.getContent());
        Assertions.assertEquals(post.getCreatedAt(), postDto.getCreatedAt());
        Assertions.assertEquals(post.getOwner().getId(), postDto.getOwnerId());
        Assertions.assertEquals(post.getOwner().getUsername(), postDto.getOwnerName());
        Assertions.assertEquals(post.getLikesNumber(), postDto.getLikesNumber());

    }

    @Test
    public void given_postDto_should_return_post(){
        //given

        Mockito.when(appUserRepository.findById(25L)).thenReturn(Optional.of(appUser));

        PostDto postDto = new PostDto();
        postDto.setId(11L);
        postDto.setContent("Hello tests");
        postDto.setCreatedAt(LocalDateTime.now());
        postDto.setOwnerId(25L);
        postDto.setLikesNumber(15);


        //when
        Post post = mapper.map(postDto);

        //then
        Assertions.assertEquals(postDto.getId(), post.getId());
        Assertions.assertEquals(postDto.getContent(), post.getContent());
        Assertions.assertEquals(postDto.getCreatedAt(), post.getCreatedAt());
        Assertions.assertEquals(postDto.getOwnerId(), post.getOwner().getId());
        Assertions.assertEquals(postDto.getOwnerName(), post.getOwner().getUsername());
        Assertions.assertEquals(postDto.getLikesNumber(), post.getLikesNumber());

    }



}
