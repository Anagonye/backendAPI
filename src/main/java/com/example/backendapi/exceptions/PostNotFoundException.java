package com.example.backendapi.exceptions;

public class PostNotFoundException extends RuntimeException{
    public PostNotFoundException(Long id) {
       super("Could not find post with id: " + id);
    }
}
