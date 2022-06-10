package com.example.test.integration.Comment.service;

import com.example.backendapi.BackendApiApplication;
import com.example.backendapi.comment.CommentDto;
import com.example.backendapi.comment.CommentNewContentRequest;
import com.example.backendapi.comment.CommentService;
import com.example.backendapi.comment.CreateCommentRequest;
import com.example.backendapi.exceptions.PostNotFoundException;
import com.example.backendapi.post.Post;
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
import org.springframework.context.annotation.Profile;
import org.springframework.security.test.context.support.WithMockUser;

@SpringBootTest(classes = BackendApiApplication.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Profile("test")
public class CommentServiceAddIT {

    @Autowired
    private AppUserService appUserService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private PostRepository postRepository;

    private Post savedPost;

    @BeforeAll
    void initTestUserAndPost(){
        AppUser appUser = new AppUser(
                "jacek20",
                "placek40",
                "jacek@test.pl",
                "USER");
        appUserService.singUpUser(appUser);

        Post post = new Post("test post", appUser);

        savedPost = postRepository.save(post);

    }



    @Test
    @WithMockUser("jacek20")
    void test_comment_is_saved_with_current_logged_in_user_information(){
        //given
        CreateCommentRequest request = new CreateCommentRequest();
        request.setPostId(savedPost.getId());
        request.setContent("test comment");
        AppUser user = (AppUser) appUserService.loadUserByUsername("jacek20");
        //when
        CommentDto savedComment = commentService.add(request);
        //then
        Assertions.assertEquals(user.getId(), savedComment.getOwnerId());
        Assertions.assertEquals(user.getUsername(), savedComment.getOwnerName());

    }

    @Test
    @WithMockUser("jacek20")
    void test_comment_is_saved_correctly_from_create_post_request(){
        //given
        CreateCommentRequest request = new CreateCommentRequest();
        request.setPostId(savedPost.getId());
        request.setContent("test comment");
        //when
        CommentDto savedComment = commentService.add(request);
        //then
        Assertions.assertEquals(request.getPostId(), savedComment.getPostId());
        Assertions.assertEquals(request.getContent(), savedComment.getContent());
    }

    @Test
    @WithMockUser
    void test_user_tries_to_save_comment_to_post_than_does_not_exist_should_throw_post_not_found_exception(){
        //given
        Long notExistPostId = 1234L;
        Assertions.assertFalse(postRepository.findById(notExistPostId).isPresent());

        CreateCommentRequest request = new CreateCommentRequest();
        request.setPostId(notExistPostId);
        request.setContent("test comment");
        //then
        Assertions.assertThrows(PostNotFoundException.class, () -> commentService.add(request));

    }



}
