package com.example.test.integration.Post.controller;

import com.example.backendapi.BackendApiApplication;
import com.example.backendapi.post.*;
import com.example.backendapi.user.AppUser;
import com.example.backendapi.user.AppUserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
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

import java.time.LocalDateTime;

@SpringBootTest(classes = BackendApiApplication.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Profile("test")
public class PostControllerEditIT {

    @Autowired
    private AppUserService appUserService;

    @Autowired
    private PostService postService;

    @Autowired
    PostRepository postRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

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
    void test_owner_tries_to_edit_post_should_return_204_no_content() throws Exception{
        //given
        CreatePostRequest createPostRequest = new CreatePostRequest();
        createPostRequest.setContent("test text");
        Long savedPostId = postService.add(createPostRequest).getId();

        PostNewContentRequest newContentRequest = new PostNewContentRequest();
        newContentRequest.setNewContent("new content test");

        mockMvc.perform(MockMvcRequestBuilders.patch("/posts/" + savedPostId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newContentRequest)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());

    }

    @Test
    @WithMockUser("jacek20")
    void test_owner_tries_to_edit_post_sending_blank_request_should_return_400_bad_request() throws Exception{
        //given
        CreatePostRequest createPostRequest = new CreatePostRequest();
        createPostRequest.setContent("test text");
        Long savedPostId = postService.add(createPostRequest).getId();

        PostNewContentRequest newContentRequest = new PostNewContentRequest();
        newContentRequest.setNewContent("");

        mockMvc.perform(MockMvcRequestBuilders.patch("/posts/" + savedPostId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newContentRequest)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

    }
    @Test
    @WithMockUser("jacek20")
    void test_owner_tries_to_edit_post_sending_exceeded_max_content_length_request_should_return_400_bad_request() throws Exception{
        //given
        CreatePostRequest createPostRequest = new CreatePostRequest();
        createPostRequest.setContent("test text");
        Long savedPostId = postService.add(createPostRequest).getId();

        PostNewContentRequest newContentRequest = new PostNewContentRequest();
        newContentRequest.setNewContent("j4QVECAEm5uwla6CKk0z0iCQquvtjS" +
                "P3ZP8klfZ2DToCPqmuyfUTBfLkjbG9NFu2TnyRrAkSlQ151MKzuCmroV0VJb" +
                "PEB3zPLQomFDroWy3czo1L4iinQgvPYtp9NbF3ttd0e2qFxHwbUOF4AbbdVSmUnpJGX5cX" +
                "M2dSXg8rGQUZAVKUrWsOc2unvRD3cuJMvGntIPyCkJjYA56cwfuMM6solVZegJjt8UgJ49rVonalUOe" +
                "DVO1eJO0qSYtFR9dMHL72FuqN23ah66Zir2yscoQazPv6tCwGxS9LHcb3OzwD");

        mockMvc.perform(MockMvcRequestBuilders.patch("/posts/" + savedPostId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newContentRequest)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

    }

    @Test
    @WithMockUser("jacek20")
    void test_user_tries_to_edit_post_that_does_not_exist_should_return_404_not_found() throws Exception{
        //given
        long notExistPostId = 1234L;
        PostNewContentRequest newContentRequest = new PostNewContentRequest();
        newContentRequest.setNewContent("new content test");

        //then
        mockMvc.perform(MockMvcRequestBuilders.patch("/posts/" + notExistPostId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newContentRequest)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
    @Test
    @WithMockUser("bob12")
    void test_not_owner_tries_to_edit_post_should_return_403_forbidden() throws Exception{
        //given
        Post postToSave = new Post();
        postToSave.setContent("test text 3");
        postToSave.setCreatedAt(LocalDateTime.now());
        postToSave.setOwner(owner);
        Long savedPostId = postRepository.save(postToSave).getId();

        PostNewContentRequest newContentRequest = new PostNewContentRequest();
        newContentRequest.setNewContent("new content test");
        //then
        mockMvc.perform(MockMvcRequestBuilders.patch("/posts/" + savedPostId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newContentRequest)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }





}
