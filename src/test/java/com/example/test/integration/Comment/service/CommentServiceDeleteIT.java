package com.example.test.integration.Comment.service;

import com.example.backendapi.BackendApiApplication;
import com.example.backendapi.comment.CommentDto;
import com.example.backendapi.comment.CommentService;
import com.example.backendapi.comment.CreateCommentRequest;
import com.example.backendapi.exceptions.CommentNotFoundException;
import com.example.backendapi.post.Post;
import com.example.backendapi.post.PostRepository;
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
public class CommentServiceDeleteIT {

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
    void test_delete_comment_should_correctly_remove_comment(){
        //given
        CreateCommentRequest request = new CreateCommentRequest();
        request.setPostId(savedPost.getId());
        request.setContent("test comment");
        CommentDto savedComment = commentService.add(request);
        Assertions.assertTrue(commentService.findById(savedComment.getId()).isPresent());
        //when
        commentService.delete(savedComment.getId());
        //then
        Assertions.assertFalse(commentService.findById(savedComment.getId()).isPresent());

    }

    @Test
    @WithMockUser("jacek20")
    void test_delete_when_given_comment_does_not_exist_should_throw_comment_not_found_exception(){
        //given
        Long notExistCommentId = 1234L;
        Assertions.assertFalse(commentService.findById(notExistCommentId).isPresent());
        //then
        Assertions.assertThrows(CommentNotFoundException.class, () -> commentService.delete(notExistCommentId));
    }

}
