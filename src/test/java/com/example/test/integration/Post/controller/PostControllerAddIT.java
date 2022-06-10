package com.example.test.integration.Post.controller;

import com.example.backendapi.BackendApiApplication;
import com.example.backendapi.post.CreatePostRequest;
import com.example.backendapi.user.AppUser;
import com.example.backendapi.user.AppUserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;


@SpringBootTest(classes = BackendApiApplication.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Profile("test")
public class PostControllerAddIT {


    @Autowired
    private AppUserService appUserService;

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new JsonMapper();

    @BeforeAll
    void initTestUser(){
        AppUser appUser = new AppUser(
                "jacek20",
                "placek40",
                "jacek@test.pl",
                "USER");
        appUserService.singUpUser(appUser);
    }



    @BeforeEach
    public void init(){

        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }
    @Test
    @WithMockUser("jacek20")
    public void test_add_post_should_return_status_created() throws Exception {

        //given
        CreatePostRequest postRequest = new CreatePostRequest();
        postRequest.setContent("Hello tests mvc");

        //then
        mockMvc.perform(post("/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(postRequest)))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.content").value("Hello tests mvc"))
                .andExpect(jsonPath("$.createdAt").isNotEmpty())
                .andExpect(jsonPath("$.likesNumber").value(0))
                .andExpect(jsonPath("$.ownerId").value(1L))
                .andExpect(jsonPath("$.ownerName").value("jacek20"));

    }

    







}
