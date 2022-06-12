package com.example.backendapi.comment;

import com.example.backendapi.aop.CommentOwnerAuthorization;
import com.example.backendapi.exceptions.CommentNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/comments")
@AllArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<CommentDto> addComment(@Valid @RequestBody CreateCommentRequest request){
        CommentDto savedComment = commentService.add(request);
        URI savedCommentUri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedComment.getId())
                .toUri();
        return ResponseEntity.created(savedCommentUri).body(savedComment);
    }

    @DeleteMapping("/{id}")
    @CommentOwnerAuthorization
    public ResponseEntity<?> deleteComment(@PathVariable("id") Long id){
        commentService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    @CommentOwnerAuthorization
    public ResponseEntity<?> editContent(@PathVariable("id") Long id, @Valid @RequestBody CommentNewContentRequest request){
        return commentService.editComment(id,request)
                .map(commentDto -> ResponseEntity.noContent().build())
                .orElseThrow(() -> new CommentNotFoundException(id));
    }
    @PatchMapping("/{id}/like")
    public ResponseEntity<?> likeComment(@PathVariable("id") Long id){
        return commentService.likeComment(id)
                .map(commentDto -> ResponseEntity.noContent().build())
                .orElseThrow(() ->new CommentNotFoundException(id));
    }

}
