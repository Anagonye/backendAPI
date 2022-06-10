package com.example.test.integration.Comment.controller;

import com.example.backendapi.BackendApiApplication;
import com.example.backendapi.comment.Comment;
import com.example.backendapi.comment.CommentNewContentRequest;
import com.example.backendapi.comment.CommentRepository;
import com.example.backendapi.comment.CommentService;
import com.example.backendapi.post.Post;
import com.example.backendapi.post.PostRepository;
import com.example.backendapi.user.AppUser;
import com.example.backendapi.user.AppUserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
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
public class CommentControllerEditIT {
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

    private final ObjectMapper objectMapper = new ObjectMapper();

    private MockMvc mockMvc;

    private Post savedPost;


    private AppUser owner;

    private Comment commentToEdit;

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

        commentToEdit = new Comment("test text", owner, savedPost);
        commentRepository.save(commentToEdit);
    }

    @Test
    @WithMockUser("jacek20")
    void test_owner_tries_to_edit_comment_should_return_204_no_content() throws  Exception{
        //given
        CommentNewContentRequest request = new CommentNewContentRequest();
        request.setNewContent("edited text");

        //then
        mockMvc.perform(MockMvcRequestBuilders.patch("/comments/" + commentToEdit.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @WithMockUser("jacek20")
    void test_user_tries_to_edit_comment_that_does_not_exist_should_return_404_not_fund() throws Exception{
        //given
        long notExistCommentId = 1234L;
        Assertions.assertFalse(commentService.findById(notExistCommentId).isPresent());

        CommentNewContentRequest request = new CommentNewContentRequest();
        request.setNewContent("edited text");
        //then
        mockMvc.perform(MockMvcRequestBuilders.patch("/comments/" + notExistCommentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());

    }

    @Test
    @WithMockUser("bob12")
    void test_not_owner_tries_to_edit_comment_should_return_403_forbidden() throws  Exception{
        //given
        CommentNewContentRequest request = new CommentNewContentRequest();
        request.setNewContent("edited text");

        //then
        mockMvc.perform(MockMvcRequestBuilders.patch("/comments/" + commentToEdit.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

}
