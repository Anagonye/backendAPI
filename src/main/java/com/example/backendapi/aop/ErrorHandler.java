package com.example.backendapi.aop;

import com.example.backendapi.exceptions.CommentNotFoundException;
import com.example.backendapi.exceptions.PostNotFoundException;
import com.example.backendapi.exceptions.RegistrationException;
import com.example.backendapi.responseModel.ConstraintViolationError;
import com.example.backendapi.responseModel.Response;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;


@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(AuthorizationServiceException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public Response handleAuthorizationException(AuthorizationServiceException ex){
        return new Response(ex.getMessage(), HttpStatus.FORBIDDEN, "User is authenticated but isn't authorized to perform the requested operation on the given resource.");
    }

    @ExceptionHandler(PostNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response handlePostNotFoundException(PostNotFoundException ex){
        return new Response(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(CommentNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response handleCommentNotFoundException(CommentNotFoundException ex){
        return new Response(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public List<ConstraintViolationError> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex){
        return ex.getBindingResult().getFieldErrors()
                .stream()
                .map(fieldError -> new ConstraintViolationError(fieldError.getField(), fieldError.getDefaultMessage()))
                .toList();
    }
    @ExceptionHandler(RegistrationException.class)
    @ResponseStatus(HttpStatus.OK)
    public Response handleRegistrationException(RegistrationException ex){
        return new Response(ex.getMessage(), HttpStatus.OK);
    }


}
