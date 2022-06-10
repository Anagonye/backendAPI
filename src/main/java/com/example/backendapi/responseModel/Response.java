package com.example.backendapi.responseModel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@AllArgsConstructor
public class Response {
    private String message;
    private HttpStatus status;
    private String description;

    public Response(String message, HttpStatus status) {
        this.message = message;
        this.status = status;
    }
}
