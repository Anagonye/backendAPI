package com.example.backendapi.post;

import com.example.backendapi.aop.PostOwnerAuthorization;
import com.example.backendapi.exceptions.PostNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/posts")
@AllArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping("/my")
    public List<PostDto> getMyPosts(@RequestParam(required = false) Integer pageNumber){
        return postService.findAllMyPosts(pageNumber);
    }


    @PostMapping
    public ResponseEntity<PostDto> addPost(@Valid @RequestBody CreatePostRequest request){
        PostDto savedPost = postService.add(request);
        URI savedPostUri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedPost.getId())
                .toUri();
        return ResponseEntity.created(savedPostUri).body(savedPost);
    }
    @PostOwnerAuthorization
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePost(@PathVariable("id") Long id){
        postService.delete(id);
        return ResponseEntity.noContent().build();
    }
    @PostOwnerAuthorization
    @PatchMapping("/{id}")
    public ResponseEntity<?> editContent(@PathVariable("id") Long id, @Valid @RequestBody PostNewContentRequest request){
       return postService.editContent(id, request)
               .map(postDto -> ResponseEntity.noContent().build())
               .orElseThrow(() -> new PostNotFoundException(id));

    }
    @PatchMapping("/{id}/like")
    public ResponseEntity<?> likePost(@PathVariable("id") Long id){
        return postService.likePost(id)
                .map(postDto -> ResponseEntity.noContent().build())
                .orElseThrow(() -> new PostNotFoundException(id));
    }





}
