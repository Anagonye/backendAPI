package com.example.test.integration.Comment.service;

import com.example.backendapi.BackendApiApplication;
import com.example.backendapi.comment.Comment;
import com.example.backendapi.comment.CommentDto;
import com.example.backendapi.comment.CommentRepository;
import com.example.backendapi.comment.CommentService;
import com.example.backendapi.post.Post;
import com.example.backendapi.post.PostDto;
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

import java.util.ArrayList;
import java.util.List;

@SpringBootTest(classes = BackendApiApplication.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Profile("test")
public class CommentServiceFindAllByPostIT {

    @Autowired
    private AppUserService appUserService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostRepository postRepository;

    private Post savedPost;

    @BeforeAll
    void initTestUsePostAndComments() {
        AppUser appUser = new AppUser(
                "jacek20",
                "placek40",
                "jacek@test.pl",
                "USER");
        appUserService.singUpUser(appUser);

        Post post = new Post("test post", appUser);
        savedPost = postRepository.save(post);

        Post post2 = new Post("test post 2", appUser);
        postRepository.save(post2);


        List<Comment> commentsToSave = new ArrayList<>();
        commentsToSave.add(new Comment("comment 1", appUser, post));
        commentsToSave.add(new Comment("comment 2", appUser, post2));
        commentsToSave.add(new Comment("comment 3", appUser, post));
        commentsToSave.add(new Comment("comment 4", appUser, post2));
        commentsToSave.add(new Comment("comment 5", appUser, post));
        commentsToSave.add(new Comment("comment 6", appUser, post2));
        commentsToSave.add(new Comment("comment 7", appUser, post));
        commentsToSave.add(new Comment("comment 8", appUser, post2));
        commentRepository.saveAll(commentsToSave);
    }

    @Test
    void test_find_all_by_post_id_returns_only_posts_with_given_id(){
        //given
        List<CommentDto> comments = commentService.findAllByPostId(savedPost.getId(), null);
        //then
        for(CommentDto commentDto:comments){
            Assertions.assertEquals(savedPost.getId(), commentDto.getPostId());
        }
    }

    @Test
    void test_find_all_by_post_id_returns_max_5_elements(){
        //given
        List<CommentDto> comments = commentService.findAllByPostId(savedPost.getId(), null);
        //then
        Assertions.assertTrue(comments.size() <= 5);
    }
}
