package com.example.test.integration.Comment.controller;

import com.example.backendapi.BackendApiApplication;
import com.example.backendapi.comment.Comment;
import com.example.backendapi.comment.CommentRepository;
import com.example.backendapi.comment.CommentService;
import com.example.backendapi.comment.CreateCommentRequest;
import com.example.backendapi.post.Post;
import com.example.backendapi.post.PostRepository;
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

@SpringBootTest(classes = BackendApiApplication.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Profile("test")
public class CommentControllerDeleteIT {

    @Autowired
    private AppUserService appUserService;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentService commentService;

    @Autowired
    private CommentRepository commentRepository;

    private MockMvc mockMvc;

    private Post savedPost;


    private AppUser owner;

    @BeforeAll
    void initTestUser(){
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

        savedPost = postRepository.save(new Post("test text", owner));

        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    @WithMockUser("jacek20")
    void test_owner_tries_to_delete_comment_should_return_status_204() throws Exception{
        //when
        CreateCommentRequest request = new CreateCommentRequest();
        request.setPostId(savedPost.getId());
        request.setContent("test");
        Long savedCommentId = commentService.add(request).getId();

        //when
        mockMvc.perform(MockMvcRequestBuilders.delete("/comments/" + savedCommentId))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        Assertions.assertFalse(commentService.findById(savedCommentId).isPresent());
    }

    @Test
    @WithMockUser("bob12")
    void test_not_owner_tries_to_delete_comment_should_return_status_403_forbidden() throws Exception{
        //given
        Comment commentToSave = new Comment("comment", owner, savedPost);
        Long savedCommentId = commentRepository.save(commentToSave).getId();

        //when
        mockMvc.perform(MockMvcRequestBuilders.delete("/comments/" + savedCommentId))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isForbidden());
        //then
        Assertions.assertTrue(commentService.findById(savedCommentId).isPresent());

    }

    @Test
    @WithMockUser("jacek20")
    void test_user_tries_to_delete_post_that_does_not_exist_should_return_404_not_found() throws Exception{
        //given
        long notExistCommentId = 1234L;
        Assertions.assertFalse(commentService.findById(notExistCommentId).isPresent());

        //then
        mockMvc.perform(MockMvcRequestBuilders.delete("/comments/" + notExistCommentId))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }


}
