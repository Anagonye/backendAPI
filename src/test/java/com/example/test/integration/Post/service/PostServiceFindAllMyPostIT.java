package com.example.test.integration.Post.service;

import com.example.backendapi.BackendApiApplication;
import com.example.backendapi.post.Post;
import com.example.backendapi.post.PostDto;
import com.example.backendapi.post.PostRepository;
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

import java.util.ArrayList;
import java.util.List;

@SpringBootTest(classes = BackendApiApplication.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PostServiceFindAllMyPostIT {

    @Autowired
    private PostService postService;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private AppUserService appUserService;

    @BeforeAll
    void initUsersAndTestData(){
        AppUser jacek20 = new AppUser(
                "jacek20",
                "placek40",
                "jacek@test.pl",
                "USER");
        appUserService.singUpUser(jacek20);

        AppUser bob12 = new AppUser(
                "bob12",
                "bobbo1234",
                "bob@test.pl",
                "USER");
        appUserService.singUpUser(bob12);

        List<Post> postsToSave = new ArrayList<>();
        postsToSave.add(new Post("test 1", jacek20));
        postsToSave.add(new Post("test 2", bob12));
        postsToSave.add(new Post("test 3", jacek20));
        postsToSave.add(new Post("test 4", bob12));
        postsToSave.add(new Post("test 5", jacek20));
        postsToSave.add(new Post("test 6", bob12));
        postsToSave.add(new Post("test 7", jacek20));
        postsToSave.add(new Post("test 8", jacek20));
        postsToSave.add(new Post("test 9", bob12));
        postsToSave.add(new Post("test 10", bob12));
        postsToSave.add(new Post("test 11", jacek20));
        postsToSave.add(new Post("test 12", jacek20));

        postRepository.saveAll(postsToSave);

    }

    @Test
    @WithMockUser("jacek20")
    void test_find_all_my_posts_returns_only_current_logged_in_user_posts(){
        //given
        List<PostDto> myPosts = postService.findAllMyPosts(null);
        //then
        for(PostDto postDto:myPosts){
            Assertions.assertEquals("jacek20", postDto.getOwnerName());
        }

    }

    @Test
    @WithMockUser("jacek20")
    void test_find_all_my_posts_returns_max_5_elements(){
        //given
        List<PostDto> myPost = postService.findAllMyPosts(null);
        //then
        Assertions.assertTrue(myPost.size() <= 5);
    }

}
