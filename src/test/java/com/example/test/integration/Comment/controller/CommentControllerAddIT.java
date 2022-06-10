package com.example.test.integration.Comment.controller;

import com.example.backendapi.BackendApiApplication;
import com.example.backendapi.comment.CreateCommentRequest;
import com.example.backendapi.post.Post;
import com.example.backendapi.post.PostRepository;
import com.example.backendapi.user.AppUser;
import com.example.backendapi.user.AppUserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.junit.jupiter.api.*;
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
public class CommentControllerAddIT {

    @Autowired
    private AppUserService appUserService;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private PostRepository postRepository;

    private MockMvc mockMvc;

    private Post savedPost;

    private final ObjectMapper objectMapper = new JsonMapper();

    @BeforeAll
    void initTestUser(){
        AppUser appUser = new AppUser(
                "jacek20",
                "placek40",
                "jacek@test.pl",
                "USER");
        appUserService.singUpUser(appUser);

        savedPost = postRepository.save(new Post("test text", appUser));
    }

    @BeforeEach
    void init(){
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    @WithMockUser("jacek20")
    void test_add_comment_should_return_status_created() throws Exception{
        //given
        CreateCommentRequest request = new CreateCommentRequest();
        request.setPostId(savedPost.getId());
        request.setContent("test comment");


        //then
        mockMvc.perform(MockMvcRequestBuilders.post("/comments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.postId").value(savedPost.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").value("test comment"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.ownerId").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.ownerName").value("jacek20"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.createdAt").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.likesNumber").value(0));
    }

    @Test
    @WithMockUser("jacek20")
    void test_add_comment_to_post_that_does_not_exist_should_return_status_404_not_found() throws Exception{
        //given
        Long notExistPostId = 1234L;
        Assertions.assertFalse(postRepository.findById(notExistPostId).isPresent());

        CreateCommentRequest request = new CreateCommentRequest();
        request.setPostId(notExistPostId);
        request.setContent("test comment");

        //then
        mockMvc.perform(MockMvcRequestBuilders.post("/comments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }




}
