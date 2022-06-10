package com.example.test.integration.Post.service;

import com.example.backendapi.BackendApiApplication;
import com.example.backendapi.exceptions.PostNotFoundException;
import com.example.backendapi.post.CreatePostRequest;
import com.example.backendapi.post.PostService;
import com.example.backendapi.user.AppUser;
import com.example.backendapi.user.AppUserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;

@SpringBootTest(classes = BackendApiApplication.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PostServiceDeleteIT {

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
    void test_delete_post_should_correctly_remove_post(){
        //given
        CreatePostRequest request = new CreatePostRequest();
        request.setContent("test");
        Long savedPostId = postService.add(request).getId();

        Assertions.assertTrue(postService.findById(savedPostId).isPresent());

        //when
        postService.delete(savedPostId);

        //then
        Assertions.assertFalse(postService.findById(savedPostId).isPresent());
    }

    @Test
    void test_delete_post_when_post_does_not_exist_should_throw_post_not_found_exception(){
        //given
        Long notExistPostId = 1234L;
        //when
        Assertions.assertFalse(postService.findById(notExistPostId).isPresent());
        //then
        Assertions.assertThrows(PostNotFoundException.class, () -> postService.delete(notExistPostId));
    }
}
