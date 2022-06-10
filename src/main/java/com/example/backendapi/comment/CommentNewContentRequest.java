package com.example.backendapi.comment;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
public class CommentNewContentRequest {
    @NotNull
    @NotBlank
    @Size(min = 1, max = 255)
    private String newContent;
}
