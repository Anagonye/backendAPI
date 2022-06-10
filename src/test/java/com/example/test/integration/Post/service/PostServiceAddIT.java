package com.example.test.integration.Post.service;

import com.example.backendapi.BackendApiApplication;
import com.example.backendapi.post.CreatePostRequest;
import com.example.backendapi.post.PostDto;
import com.example.backendapi.post.PostService;
import com.example.backendapi.user.AppUser;
import com.example.backendapi.user.AppUserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.security.test.context.support.WithMockUser;

@SpringBootTest(classes = BackendApiApplication.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Profile("test")
public class PostServiceAddIT {


    @Autowired
    private PostService postService;

    @Autowired
    private AppUserService appUserService;

    @BeforeAll
    public void init(){
        AppUser appUser = new AppUser(
                "jacek20",
                "placek40",
                "jacek@test.pl",
                "USER");
        appUserService.singUpUser(appUser);
    }




    @Test
    @WithMockUser("jacek20")
    public void test_post_is_saved_with_current_logged_in_user_information(){
        //given
        CreatePostRequest request = new CreatePostRequest();
        request.setContent("Hello test");
        AppUser user = (AppUser) appUserService.loadUserByUsername("jacek20");
        //when
        PostDto savedPost = postService.add(request);
        //then
        Assertions.assertEquals("jacek20", savedPost.getOwnerName());
        Assertions.assertEquals(user.getId(), savedPost.getOwnerId());
    }

    @Test
    @WithMockUser("jacek20")
    public void test_post_is_saved_correctly_from_create_post_request(){
        //given
        CreatePostRequest request = new CreatePostRequest();
        request.setContent("Hello test");
        //when
        PostDto savedPost = postService.add(request);
        //then
        Assertions.assertEquals(request.getContent(), savedPost.getContent());
    }








}
