package com.example.test.integration.Post.service;

import com.example.backendapi.BackendApiApplication;
import com.example.backendapi.post.CreatePostRequest;
import com.example.backendapi.post.PostDto;
import com.example.backendapi.post.PostNewContentRequest;
import com.example.backendapi.post.PostService;
import com.example.backendapi.user.AppUser;
import com.example.backendapi.user.AppUserService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.Optional;


@SpringBootTest(classes = BackendApiApplication.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PostServiceEditIT {

    @Autowired
    private PostService postService;

    @Autowired
    private AppUserService appUserService;


    @BeforeAll
    public void initUser(){
        AppUser appUser = new AppUser(
                "jacek20",
                "placek40",
                "jacek@test.pl",
                "USER");
        appUserService.singUpUser(appUser);
    }

    @BeforeEach
    public void createTestPost(){

    }

    @Test
    @WithMockUser("jacek20")
    void test_edit_content_correctly_should_return_edited_post(){
        //given
        CreatePostRequest createRequest = new CreatePostRequest();
        createRequest.setContent("test");
        PostDto postDto = postService.add(createRequest);
        Assertions.assertEquals("test", postDto.getContent());

        //when
        PostNewContentRequest newContentRequest = new PostNewContentRequest();
        newContentRequest.setNewContent("edit test");
        Optional<PostDto> editedPost = postService.editContent(postDto.getId(), newContentRequest);

        //then
        Assertions.assertEquals("edit test", editedPost.map(PostDto::getContent).orElseThrow());
    }

    @Test
    void test_edit_content_of_post_that_does_not_exist_should_return_empty_optional(){
        //given
        Long notExistPostId  = 1234L;
        Assertions.assertFalse(postService.findById(notExistPostId).isPresent());
        //when
        PostNewContentRequest newContentRequest = new PostNewContentRequest();
        newContentRequest.setNewContent("edit test");
        Optional<PostDto> editedPost = postService.editContent(notExistPostId, newContentRequest);
        //then
        Assertions.assertFalse(editedPost.isPresent());

    }



}
