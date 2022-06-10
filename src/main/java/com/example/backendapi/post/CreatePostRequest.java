package com.example.backendapi.post;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
public class CreatePostRequest {
    @NotNull
    @NotBlank
    @Size(min = 1, max = 1000)
    private String content;
}
