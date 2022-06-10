package com.example.test.integration.Post.controller;

import com.example.backendapi.BackendApiApplication;
import com.example.backendapi.comment.Comment;
import com.example.backendapi.comment.CommentRepository;
import com.example.backendapi.exceptions.PostNotFoundException;
import com.example.backendapi.post.*;
import com.example.backendapi.user.AppUser;
import com.example.backendapi.user.AppUserService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;

@SpringBootTest( classes = BackendApiApplication.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Profile("test")
public class PostControllerDeleteIT {

    @Autowired
    private AppUserService appUserService;

    @Autowired
    private PostService postService;

    @Autowired PostRepository postRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;


    private AppUser owner;

    @BeforeAll
    void initTestUsers(){
        owner = new AppUser(
                "jacek20",
                "placek40",
                "jacek@test.pl",
                "USER");
        appUserService.singUpUser(owner);

        AppUser notOwner = new AppUser(
                "bob12",
                "bobbo1234",
                "bob@test.pl",
                "USER");
        appUserService.singUpUser(notOwner);

    }


    @BeforeEach
    void init(){
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

    }


    @Test
    @WithMockUser("jacek20")
    void test_owner_tries_to_delete_post_with_no_comment_should_return_status_204() throws Exception {
        //given
        CreatePostRequest createPostRequest = new CreatePostRequest();
        createPostRequest.setContent("test text");
        Long savedPostId = postService.add(createPostRequest).getId();

        //when
         mockMvc.perform(MockMvcRequestBuilders.delete("/posts/" + savedPostId))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());
        //then
        Assertions.assertFalse(postService.findById(savedPostId).isPresent());
    }

    @Test
    @WithMockUser("jacek20")
    void test_owner_tries_to_delete_post_with_comments_should_return_status_204_no_content() throws Exception {
        //given
        CreatePostRequest createPostRequest = new CreatePostRequest();
        createPostRequest.setContent("test text 2");
        Long savedPostId = postService.add(createPostRequest).getId();

        Post post = postRepository.findById(savedPostId)
                .orElseThrow(() -> new PostNotFoundException(savedPostId));

        Comment comment1 = new Comment();
        comment1.setPost(post);
        comment1.setOwner(owner);
        comment1.setContent("test comment");
        comment1.setCreatedAt(LocalDateTime.now());
        commentRepository.save(comment1);

        //when
        mockMvc.perform(MockMvcRequestBuilders.delete("/posts/" + savedPostId))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        Assertions.assertFalse(postService.findById(savedPostId).isPresent());
    }


    @Test
    @WithMockUser("bob12")
    void test_not_owner_tries_to_delete_post_should_return_status_403_forbidden() throws Exception {
        //given
        Post postToSave = new Post();
        postToSave.setContent("test text 3");
        postToSave.setCreatedAt(LocalDateTime.now());
        postToSave.setOwner(owner);
        Long savedPostId = postRepository.save(postToSave).getId();

        //when
        mockMvc.perform(MockMvcRequestBuilders.delete("/posts/" + savedPostId))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isForbidden());
        //then
        Assertions.assertTrue(postService.findById(savedPostId).isPresent());
    }


    @Test
    @WithMockUser("jacek20")
    void test_user_tries_to_delete_post_that_does_not_exist_should_return_404_not_found() throws Exception {
        //given
        long notExistPostId = 1234L;
        Assertions.assertFalse(postService.findById(notExistPostId).isPresent());

        //then
        mockMvc.perform(MockMvcRequestBuilders.delete("/posts/" + notExistPostId))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());

    }



}
