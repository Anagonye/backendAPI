package com.example.backendapi.post;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
public class PostNewContentRequest {
    @NotNull
    @NotBlank
    @Size(min = 1, max = 255)
    private String newContent;
}
