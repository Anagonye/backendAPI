package com.example.backendapi.exceptions;

public class CommentNotFoundException extends RuntimeException{
    public CommentNotFoundException(Long id) {
        super("Could not find comment with id: " + id);
    }
}
