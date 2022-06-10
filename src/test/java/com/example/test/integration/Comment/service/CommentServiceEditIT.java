package com.example.test.integration.Comment.service;

import com.example.backendapi.BackendApiApplication;
import com.example.backendapi.comment.CommentDto;
import com.example.backendapi.comment.CommentNewContentRequest;
import com.example.backendapi.comment.CommentService;
import com.example.backendapi.comment.CreateCommentRequest;
import com.example.backendapi.exceptions.CommentNotFoundException;
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

import java.util.Optional;

@SpringBootTest(classes = BackendApiApplication.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Profile("test")
public class CommentServiceEditIT {

    @Autowired
    private AppUserService appUserService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private PostRepository postRepository;

    private Post savedPost;

    @BeforeAll
    void initTestUserAndPost() {
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
    void test_edit_comment_correctly_should_return_edited_comment(){
        //given
        CreateCommentRequest createRequest = new CreateCommentRequest();
        createRequest.setPostId(savedPost.getId());
        createRequest.setContent("test comment");
        CommentDto savedComment = commentService.add(createRequest);

        //when
        CommentNewContentRequest newContentRequest = new CommentNewContentRequest();
        newContentRequest.setNewContent("edited comment");
        Optional<CommentDto> editComment = commentService.editComment(savedComment.getId(),newContentRequest);

        //then
        Assertions.assertEquals("edited comment",editComment.map(CommentDto::getContent).orElseThrow());

    }

    @Test
    void test_edit_comment_that_does_not_exist_should_return_empty_optional(){
        //given
        Long notExistCommentId = 1234L;
        Assertions.assertFalse(commentService.findById(notExistCommentId).isPresent());
        CommentNewContentRequest commentNewContentRequest = new CommentNewContentRequest();
        commentNewContentRequest.setNewContent("test");
        //when
        Optional<CommentDto> editedComment = commentService.editComment(notExistCommentId,commentNewContentRequest);
        //then
        Assertions.assertFalse(editedComment.isPresent());

    }
}
