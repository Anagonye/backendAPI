package com.example.backendapi.exceptions;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException(String email) {
        super("User with email: "+email+" not found");
    }
}
